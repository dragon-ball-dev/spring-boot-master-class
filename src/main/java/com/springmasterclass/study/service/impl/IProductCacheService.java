package com.springmasterclass.study.service.impl;

import com.springmasterclass.study.dto.record.CacheBenchmarkResponse;
import com.springmasterclass.study.dto.record.ProductCacheResponse;
import com.springmasterclass.study.service.ProductCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class IProductCacheService implements ProductCacheService {

    private final CacheManager cacheManager;

    @Lazy
    @Autowired
    private ProductCacheService self;

    // Giả lập bảng lưu trữ dữ liệu trong Database
    private static final Map<Long, ProductCacheResponse> MOCK_DATABASE = new ConcurrentHashMap<>();

    static {
        MOCK_DATABASE.put(101L, new ProductCacheResponse(
                101L, "MacBook Pro M3 Max 16-inch", new BigDecimal("79990000"),
                "Apple M3 Max chip with 16-core CPU and 40-core GPU", 25,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        ));
        MOCK_DATABASE.put(102L, new ProductCacheResponse(
                102L, "iPhone 16 Pro Max 256GB", new BigDecimal("34990000"),
                "Titanium frame with A18 Pro Bionic Chip", 50,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        ));
        MOCK_DATABASE.put(103L, new ProductCacheResponse(
                103L, "Sony WH-1000XM5 Wireless Headphones", new BigDecimal("8490000"),
                "Industry-leading noise canceling headphones with Auto NC Optimizer", 40,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        ));
    }

    /**
     * Demo @Cacheable:
     * Lần đầu gọi: Spring Cache kiểm tra Redis -> Cache MISS -> Thực thi hàm (giả lập DB 200ms) -> Ghi kết quả vào Redis.
     * Các lần sau: Spring Cache thấy key trong Redis -> Cache HIT -> Trả kết quả ngay lập tức (1-3ms), KHÔNG chạy code trong hàm này.
     */
    @Override
    @Cacheable(value = "product_detail", key = "#id", unless = "#result == null")
    public ProductCacheResponse getProductById(Long id) {
        log.info("[CACHE MISS] Đang truy vấn Database cho Product ID: {}...", id);
        
        // Giả lập độ trễ truy vấn Database (200ms delay cho SQL Query / Index Scan / Join)
        simulateDatabaseDelay(200);

        ProductCacheResponse product = MOCK_DATABASE.get(id);
        if (product == null) {
            log.warn("Không tìm thấy Product ID: {} trong Database", id);
            return null;
        }

        // Tạo bản ghi mới cập nhật lại thời điểm được nạp vào Cache
        return new ProductCacheResponse(
                product.id(), product.name(), product.price(),
                product.description(), product.stock(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    /**
     * Benchmark trực quan so sánh tốc độ DB vs Redis Cache & hiển thị số liệu giảm tải.
     */
    @Override
    public CacheBenchmarkResponse getProductWithBenchmark(Long id) {
        Cache cache = cacheManager.getCache("product_detail");
        Cache.ValueWrapper valueWrapper = cache != null ? cache.get(id) : null;
        boolean isCacheHit = (valueWrapper != null && valueWrapper.get() != null);

        long startTime = System.currentTimeMillis();

        // Gọi qua proxy self để AOP @Cacheable được kích hoạt
        ProductCacheResponse product = self.getProductById(id);

        long executionTime = System.currentTimeMillis() - startTime;

        if (isCacheHit) {
            return new CacheBenchmarkResponse(
                    product,
                    "REDIS_CACHE (Cache HIT)",
                    executionTime,
                    "99%",
                    "Dữ liệu được lấy trực tiếp từ In-Memory Redis Cache. Database hoàn toàn KHÔNG phải xử lý truy vấn!"
            );
        } else {
            return new CacheBenchmarkResponse(
                    product,
                    "DATABASE (Cache MISS)",
                    executionTime,
                    "0%",
                    "Lần đầu truy vấn: Hệ thống phải đọc từ Database (tốn ~200ms). Dữ liệu đã được tự động lưu vào Redis Cache cho các lần sau!"
            );
        }
    }

    /**
     * Demo @CachePut:
     * Luôn luôn thực thi hàm này (cập nhật DB), sau đó tự động CẬP NHẬT đè giá trị mới vào Redis Cache.
     */
    @Override
    @CachePut(value = "product_detail", key = "#id")
    public ProductCacheResponse updateProduct(Long id, ProductCacheResponse request) {
        log.info("[CACHE PUT] Đang cập nhật Database & làm mới Redis Cache cho Product ID: {}", id);

        ProductCacheResponse updatedProduct = new ProductCacheResponse(
                id,
                request.name(),
                request.price(),
                request.description(),
                request.stock(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        // Cập nhật Database
        MOCK_DATABASE.put(id, updatedProduct);

        return updatedProduct;
    }

    /**
     * Demo @CacheEvict:
     * Xóa sản phẩm trong Database đồng thời XÓA KEY tương ứng khỏi Redis Cache để tránh stale data.
     */
    @Override
    @CacheEvict(value = "product_detail", key = "#id")
    public void deleteProduct(Long id) {
        log.info("[CACHE EVICT] Đang xóa Product ID: {} khỏi Database & xóa key khỏi Redis Cache", id);
        MOCK_DATABASE.remove(id);
    }

    /**
     * Demo @CacheEvict(allEntries = true):
     * Xóa TOÀN BỘ các key nằm trong namespace 'product_detail'.
     */
    @Override
    @CacheEvict(value = "product_detail", allEntries = true)
    public void clearAllCache() {
        log.info("[CACHE EVICT ALL] Đang dọn dẹp sạch sẽ toàn bộ Cache thuộc namespace 'product_detail'");
    }

    private void simulateDatabaseDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
