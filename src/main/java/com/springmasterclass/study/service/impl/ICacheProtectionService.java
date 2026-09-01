package com.springmasterclass.study.service.impl;

import com.springmasterclass.study.dto.record.CacheProtectionResponse;
import com.springmasterclass.study.dto.record.ProductCacheResponse;
import com.springmasterclass.study.service.CacheProtectionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ICacheProtectionService implements CacheProtectionService {

    private final RedissonClient redissonClient;
    private final RedisTemplate<String, Object> redisTemplate;

    private RBloomFilter<Long> productBloomFilter;

    @PostConstruct
    public void init() {
        // Khởi tạo Bloom Filter trên Redis: chứa tối đa 100,000 phần tử, tỷ lệ báo sai (false positive) = 1%
        productBloomFilter = redissonClient.getBloomFilter("bloom:products");
        productBloomFilter.tryInit(100_000L, 0.01);
        
        // Thêm một số ID sản phẩm hợp lệ mẫu (101, 102, 103, 104, 105)
        productBloomFilter.add(101L);
        productBloomFilter.add(102L);
        productBloomFilter.add(103L);
        productBloomFilter.add(104L);
        productBloomFilter.add(105L);
        log.info("Đã khởi tạo Bloom Filter 'bloom:products' thành công với các ID hợp lệ [101 - 105]");
    }

    @Override
    public void initBloomFilterData() {
        productBloomFilter.add(101L);
        productBloomFilter.add(102L);
        productBloomFilter.add(103L);
    }

    /**
     * 1. CHỐNG CACHE PENETRATION DÙNG BLOOM FILTER
     */
    @Override
    public CacheProtectionResponse getProductWithBloomFilter(Long productId) {
        long startTime = System.currentTimeMillis();

        // Bước 1: Kiểm tra xem ID có trong Bloom Filter hay không
        boolean contains = productBloomFilter.contains(productId);
        if (!contains) {
            log.warn("[BloomFilter] Chặn ngay lập tức ID không tồn tại: {}. Không truy vấn Redis/DB!", productId);
            long executionTime = System.currentTimeMillis() - startTime;
            return new CacheProtectionResponse(
                    false,
                    "Chặn Cache Penetration: Sản phẩm ID " + productId + " không tồn tại trong hệ thống!",
                    null,
                    "BLOOM_FILTER_BLOCKED",
                    executionTime
            );
        }

        // Bước 2: ID hợp lệ -> Kiểm tra Redis Cache
        String cacheKey = "cache:product:" + productId;
        Object cachedValue = redisTemplate.opsForValue().get(cacheKey);

        if (cachedValue != null) {
            long executionTime = System.currentTimeMillis() - startTime;
            return new CacheProtectionResponse(
                    true,
                    "Lấy dữ liệu từ Redis Cache thành công!",
                    cachedValue,
                    "REDIS_CACHE_HIT",
                    executionTime
            );
        }

        // Bước 3: Cache Miss -> Giả lập query Database và lưu Cache
        ProductCacheResponse dbProduct = queryDatabaseSimulated(productId);
        redisTemplate.opsForValue().set(cacheKey, dbProduct, Duration.ofMinutes(10));

        long executionTime = System.currentTimeMillis() - startTime;
        return new CacheProtectionResponse(
                true,
                "Lấy dữ liệu từ Database & Cập nhật Cache thành công!",
                dbProduct,
                "DATABASE_QUERY_&_CACHE_POPULATED",
                executionTime
        );
    }

    /**
     * 2. CHỐNG CACHE AVALANCHE DÙNG RANDOM JITTER TTL
     */
    @Override
    public CacheProtectionResponse cacheProductWithJitterTTL(Long productId) {
        long startTime = System.currentTimeMillis();
        String cacheKey = "cache:product:jitter:" + productId;

        // Giả lập lấy dữ liệu
        ProductCacheResponse product = queryDatabaseSimulated(productId);

        // Tính toán TTL cơ sở = 300s (5 phút) + Random Jitter từ 1 đến 60 giây
        int baseTtlSeconds = 300;
        int randomJitterSeconds = ThreadLocalRandom.current().nextInt(1, 61);
        int finalTtlSeconds = baseTtlSeconds + randomJitterSeconds;

        // Lưu vào Redis với TTL ngẫu nhiên
        redisTemplate.opsForValue().set(cacheKey, product, Duration.ofSeconds(finalTtlSeconds));

        long executionTime = System.currentTimeMillis() - startTime;
        log.info("[CacheAvalanche] Đã lưu Cache với Random Jitter TTL: {} giây (Base: 300s + Jitter: {}s)",
                finalTtlSeconds, randomJitterSeconds);

        return new CacheProtectionResponse(
                true,
                "Đã lưu Cache thành công với TTL ngẫu nhiên: " + finalTtlSeconds + "s để chống Tuyết Lở!",
                product,
                "RANDOM_JITTER_TTL_APPLIED (TTL: " + finalTtlSeconds + "s)",
                executionTime
        );
    }

    /**
     * 3. CHỐNG CACHE STAMPEDE DÙNG REDISSON LOCK + DOUBLE CHECK CACHE
     */
    @Override
    public CacheProtectionResponse getProductWithAntiStampede(Long productId) {
        long startTime = System.currentTimeMillis();
        String cacheKey = "cache:product:hot:" + productId;

        // Lần 1: Kiểm tra Cache
        Object cachedValue = redisTemplate.opsForValue().get(cacheKey);
        if (cachedValue != null) {
            long executionTime = System.currentTimeMillis() - startTime;
            return new CacheProtectionResponse(
                    true,
                    "Lấy dữ liệu Hot Key từ Redis Cache thành công!",
                    cachedValue,
                    "HOT_KEY_CACHE_HIT",
                    executionTime
            );
        }

        // Cache Miss -> Dùng Redisson Lock để chỉ cho phép 1 luồng duy nhất xuống DB nạp lại Cache
        String lockKey = "lock:stampede:product:" + productId;
        RLock lock = redissonClient.getLock(lockKey);
        boolean isAcquired = false;

        try {
            // Chờ tối đa 3s để lấy Lock, tự nhả Lock sau 5s
            isAcquired = lock.tryLock(3, 5, TimeUnit.SECONDS);

            if (!isAcquired) {
                // Luồng khác chưa lấy được lock -> Chờ 50ms rồi thử đọc lại từ Cache (vừa được luồng trước nạp)
                Thread.sleep(50);
                Object retryCache = redisTemplate.opsForValue().get(cacheKey);
                long executionTime = System.currentTimeMillis() - startTime;
                return new CacheProtectionResponse(
                        true,
                        "Lấy dữ liệu từ Cache sau khi luồng trước nạp xong!",
                        retryCache,
                        "STAMPEDE_RETRY_CACHE_HIT",
                        executionTime
                );
            }

            // Lần 2 (Double Check Cache): Kiểm tra lại xem luồng trước đó đã nạp Cache chưa
            Object doubleCheckCache = redisTemplate.opsForValue().get(cacheKey);
            if (doubleCheckCache != null) {
                long executionTime = System.currentTimeMillis() - startTime;
                return new CacheProtectionResponse(
                        true,
                        "Lấy dữ liệu từ Cache qua Double Check!",
                        doubleCheckCache,
                        "DOUBLE_CHECK_CACHE_HIT",
                        executionTime
                );
            }

            // Chỉ DUY NHẤT 1 LUỒNG chạy xuống Database!
            log.info("🔥 [CacheStampede] Luồng sở hữu Lock đang xuống DB query dữ liệu cho Hot Key: {}", productId);
            ProductCacheResponse dbProduct = queryDatabaseSimulated(productId);

            // Nạp lại vào Redis Cache với TTL 10 phút
            redisTemplate.opsForValue().set(cacheKey, dbProduct, Duration.ofMinutes(10));

            long executionTime = System.currentTimeMillis() - startTime;
            return new CacheProtectionResponse(
                    true,
                    "Luồng lấy Lock duy nhất đã query DB & nạp Cache thành công!",
                    dbProduct,
                    "STAMPEDE_SINGLE_DB_QUERY_SUCCESS",
                    executionTime
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Xảy ra lỗi gián đoạn luồng khi chờ Lock Stampede", e);
        } finally {
            if (isAcquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private ProductCacheResponse queryDatabaseSimulated(Long productId) {
        try {
            Thread.sleep(100); // Giả lập độ trễ 100ms khi query MySQL Database
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new ProductCacheResponse(
                productId,
                "Sản Phẩm Cao Cấp #" + productId,
                new BigDecimal("19990000"),
                "Mô tả chi tiết sản phẩm được nạp từ Database",
                100,
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}
