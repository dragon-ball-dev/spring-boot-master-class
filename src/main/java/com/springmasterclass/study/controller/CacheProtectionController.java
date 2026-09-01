package com.springmasterclass.study.controller;

import com.springmasterclass.study.common.ApiResponse;
import com.springmasterclass.study.common.BaseController;
import com.springmasterclass.study.dto.record.CacheProtectionResponse;
import com.springmasterclass.study.service.CacheProtectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cache-protection")
@RequiredArgsConstructor
public class CacheProtectionController extends BaseController {

    private final CacheProtectionService cacheProtectionService;

    // 1. Kiểm thử chống Cache Penetration (Thủng Cache) bằng Bloom Filter
    @GetMapping("/bloom-filter/{productId}")
    public ApiResponse<CacheProtectionResponse> getProductWithBloomFilter(@PathVariable Long productId) {
        CacheProtectionResponse response = cacheProtectionService.getProductWithBloomFilter(productId);
        return ApiResponse.success(response);
    }

    // 2. Kiểm thử chống Cache Avalanche (Tuyết Lở Cache) bằng Random Jitter TTL
    @PostMapping("/jitter-ttl/{productId}")
    public ApiResponse<CacheProtectionResponse> cacheProductWithJitterTTL(@PathVariable Long productId) {
        CacheProtectionResponse response = cacheProtectionService.cacheProductWithJitterTTL(productId);
        return ApiResponse.success(response);
    }

    // 3. Kiểm thử chống Cache Stampede (Đám Đông Giẫm Đạp) bằng Redisson Lock
    @GetMapping("/stampede-lock/{productId}")
    public ApiResponse<CacheProtectionResponse> getProductWithAntiStampede(@PathVariable Long productId) {
        CacheProtectionResponse response = cacheProtectionService.getProductWithAntiStampede(productId);
        return ApiResponse.success(response);
    }
}
