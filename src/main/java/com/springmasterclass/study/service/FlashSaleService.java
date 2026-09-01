package com.springmasterclass.study.service;

import com.springmasterclass.study.dto.record.FlashSaleResponse;

public interface FlashSaleService {

    // Reset kho hàng (ví dụ: 10 sản phẩm)
    void resetStock(Long productId, int initialStock);

    // Lấy thông tin kho hiện tại
    int getStock(Long productId);

    // Mua 1 sản phẩm KHÔNG dùng Lock (Dễ bị Race Condition)
    boolean buyWithoutLock(Long productId);

    // Mua 1 sản phẩm CÓ dùng Redisson Distributed Lock
    boolean buyWithRedissonLock(Long productId);

    // Giả lập N luồng đồng thời bấm Mua để so sánh Race Condition vs Distributed Lock
    FlashSaleResponse runConcurrentTest(Long productId, boolean useLock, int totalRequests);
}
