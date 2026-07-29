package com.springmasterclass.study.controller;

import com.springmasterclass.study.common.ApiResponse;
import com.springmasterclass.study.common.BaseController;
import com.springmasterclass.study.dto.record.CacheBenchmarkResponse;
import com.springmasterclass.study.dto.record.ProductCacheResponse;
import com.springmasterclass.study.service.ProductCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cache-demo/products")
@RequiredArgsConstructor
public class ProductCacheController extends BaseController {

    private final ProductCacheService productCacheService;

    // Demo @Cacheable: Lấy thông tin sản phẩm (được lưu cache tự động)
    @GetMapping("/{id}")
    public ApiResponse<ProductCacheResponse> getProductById(@PathVariable Long id) {
        ProductCacheResponse product = productCacheService.getProductById(id);
        if (product == null) {
            return ApiResponse.error(404, "Không tìm thấy sản phẩm với ID: " + id);
        }
        return ApiResponse.success(product);
    }

    // Benchmark API: Hiển thị trực quan chỉ số hiệu năng (Cache MISS vs Cache HIT)
    @GetMapping("/{id}/benchmark")
    public ApiResponse<CacheBenchmarkResponse> getProductBenchmark(@PathVariable Long id) {
        CacheBenchmarkResponse benchmark = productCacheService.getProductWithBenchmark(id);
        if (benchmark.product() == null) {
            return ApiResponse.error(404, "Không tìm thấy sản phẩm với ID: " + id);
        }
        return ApiResponse.success(benchmark);
    }

    // Demo @CachePut: Cập nhật sản phẩm & đồng bộ ngay lập tức vào Cache
    @PutMapping("/{id}")
    public ApiResponse<ProductCacheResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductCacheResponse request
    ) {
        ProductCacheResponse updatedProduct = productCacheService.updateProduct(id, request);
        return ApiResponse.success(updatedProduct);
    }

    // Demo @CacheEvict: Xóa sản phẩm & làm sạch key tương ứng trong Cache
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteProduct(@PathVariable Long id) {
        productCacheService.deleteProduct(id);
        return ApiResponse.success("Đã xóa sản phẩm ID: " + id + " và xóa Cache thành công!");
    }

    // Demo @CacheEvict(allEntries = true): Xóa toàn bộ Cache namespace 'product_detail'
    @DeleteMapping("/clear-all")
    public ApiResponse<String> clearAllCache() {
        productCacheService.clearAllCache();
        return ApiResponse.success("Đã làm sạch toàn bộ Cache 'product_detail' thành công!");
    }
}
