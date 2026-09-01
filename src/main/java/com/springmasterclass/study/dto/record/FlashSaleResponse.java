package com.springmasterclass.study.dto.record;

import java.io.Serializable;

public record FlashSaleResponse(
        boolean success,
        String message,
        int remainingStock,
        int totalRequestsProcessed,
        int successfulOrders,
        int oversoldAmount,
        long executionTimeMs,
        String lockMode
) implements Serializable {
}
