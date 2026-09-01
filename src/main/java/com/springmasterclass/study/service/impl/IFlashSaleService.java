package com.springmasterclass.study.service.impl;

import com.springmasterclass.study.anotation.DistributedLock;
import com.springmasterclass.study.dto.record.FlashSaleResponse;
import com.springmasterclass.study.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class IFlashSaleService implements FlashSaleService {

    @Autowired
    @Lazy
    private FlashSaleService self;

    // Giả lập tồn kho trong Database cho từng Product ID
    private static final Map<Long, Integer> STOCK_DB = new ConcurrentHashMap<>();

    static {
        STOCK_DB.put(101L, 10); // Mặc định có 10 chiếc iPhone 16 Pro Max
    }

    @Override
    public void resetStock(Long productId, int initialStock) {
        STOCK_DB.put(productId, initialStock);
        log.info("🔄 Reset tồn kho cho Product ID: {} về {} sản phẩm", productId, initialStock);
    }

    @Override
    public int getStock(Long productId) {
        return STOCK_DB.getOrDefault(productId, 0);
    }

    /**
     * KHÔNG DÙNG LOCK (Unsafe):
     * Nhiều luồng đọc cùng lúc giá trị tồn kho > 0 -> cùng trừ 1 -> Dẫn đến Race Condition & Bán âm kho (Overselling).
     */
    @Override
    public boolean buyWithoutLock(Long productId) {
        int currentStock = STOCK_DB.getOrDefault(productId, 0);

        if (currentStock > 0) {
            // Giả lập độ trễ 5ms xử lý tạo đơn hàng trong DB (khiến Race Condition diễn ra rõ rệt)
            simulateProcessingDelay(5);

            // Giả lập SQL: UPDATE product SET stock = stock - 1 trong DB (không lock) -> làm âm kho
            STOCK_DB.compute(productId, (k, v) -> (v == null ? 0 : v) - 1);
            return true;
        }
        return false;
    }

    /**
     * CÓ DÙNG REDISSON DISTRIBUTED LOCK (Safe):
     * Chỉ duy nhất 1 luồng được vào trừ kho tại một thời điểm (kể cả khi chạy trên nhiều Server khác nhau).
     */
    @Override
    @DistributedLock(key = "'lock:flash_sale:' + #productId", waitTime = 5, leaseTime = 10)
    public boolean buyWithRedissonLock(Long productId) {
        int currentStock = STOCK_DB.getOrDefault(productId, 0);

        if (currentStock > 0) {
            simulateProcessingDelay(5);
            STOCK_DB.put(productId, currentStock - 1);
            return true;
        }
        return false;
    }

    /**
     * Giả lập N luồng đồng thời (VD: 100 User) bấm "MUA NGAY" trong cùng 1 millisecond.
     */
    @Override
    public FlashSaleResponse runConcurrentTest(Long productId, boolean useLock, int totalRequests) {
        // Reset tồn kho về 10 trước khi test
        resetStock(productId, 10);

        ExecutorService executorService = Executors.newFixedThreadPool(totalRequests);
        CountDownLatch readyLatch = new CountDownLatch(totalRequests); // Chờ tất cả 100 luồng chuẩn bị xong
        CountDownLatch startLatch = new CountDownLatch(1);             // Phát lệnh nổ súng cho 100 luồng bắn cùng lúc
        CountDownLatch finishLatch = new CountDownLatch(totalRequests);// Chờ 100 luồng chạy xong

        AtomicInteger successfulOrders = new AtomicInteger(0);
        AtomicInteger failedOrders = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalRequests; i++) {
            executorService.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await(); // Chờ phát lệnh đồng loạt

                    boolean isPurchased;
                    if (useLock) {
                        // Gọi qua self proxy để AOP @DistributedLock có hiệu lực
                        isPurchased = self.buyWithRedissonLock(productId);
                    } else {
                        isPurchased = buyWithoutLock(productId);
                    }

                    if (isPurchased) {
                        successfulOrders.incrementAndGet();
                    } else {
                        failedOrders.incrementAndGet();
                    }
                } catch (Exception e) {
                    failedOrders.incrementAndGet();
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        try {
            readyLatch.await(); // Chờ 100 luồng sẵn sàng
            startLatch.countDown(); // NỔ SÚNG! 100 luồng cùng chạy 1 lúc
            finishLatch.await(); // Chờ 100 luồng hoàn tất
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executorService.shutdown();
        }

        long executionTime = System.currentTimeMillis() - startTime;
        int remainingStock = getStock(productId);
        
        // Nếu không lock, số lượng đơn mua thành công có thể vượt quá 10 (bán lố), hoặc tồn kho bị âm
        int oversoldAmount = Math.max(0, successfulOrders.get() - 10);
        if (remainingStock < 0) {
            oversoldAmount += Math.abs(remainingStock);
        }

        String mode = useLock ? "REDISSON DISTRIBUTED LOCK (Safe)" : "NO LOCK (Unsafe - Race Condition)";
        String message = useLock 
                ? "Thành công 100%! Đúng 10 đơn mua thành công, tồn kho còn 0. Không ai mua lố!" 
                : "CẢNH BÁO! Xảy ra hiện tượng Overselling (Bán lố hàng/Âm kho) do không dùng Lock!";

        return new FlashSaleResponse(
                oversoldAmount == 0,
                message,
                remainingStock,
                totalRequests,
                successfulOrders.get(),
                oversoldAmount,
                executionTime,
                mode
        );
    }

    private void simulateProcessingDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
