package com.springmasterclass.study.service;

import com.springmasterclass.study.dto.record.CacheProtectionResponse;

public interface CacheProtectionService {

    // 1. Chống Cache Penetration bằng Bloom Filter
    CacheProtectionResponse getProductWithBloomFilter(Long productId);

    // 2. Chống Cache Avalanche bằng Random Jitter TTL
    CacheProtectionResponse cacheProductWithJitterTTL(Long productId);

    // 3. Chống Cache Stampede bằng Redisson Lock + Double Check Cache
    CacheProtectionResponse getProductWithAntiStampede(Long productId);

    // Helper: Nạp ID hợp lệ vào Bloom Filter
    void initBloomFilterData();
}
