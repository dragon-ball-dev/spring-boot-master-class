package com.springmasterclass.study.controller;

import com.springmasterclass.study.common.ApiResponse;
import com.springmasterclass.study.common.BaseController;
import com.springmasterclass.study.dto.record.FlashSaleResponse;
import com.springmasterclass.study.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/flash-sale")
@RequiredArgsConstructor
public class FlashSaleController extends BaseController {

    private final FlashSaleService flashSaleService;

    // Reset kho hàng
    @PostMapping("/reset/{productId}")
    public ApiResponse<String> resetStock(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "10") int stock
    ) {
        flashSaleService.resetStock(productId, stock);
        return ApiResponse.success("Đã reset kho cho sản phẩm ID " + productId + " về " + stock + " sản phẩm.");
    }

    // Lấy số lượng tồn kho hiện tại
    @GetMapping("/stock/{productId}")
    public ApiResponse<Integer> getStock(@PathVariable Long productId) {
        return ApiResponse.success(flashSaleService.getStock(productId));
    }

    // Mua 1 đơn lẻ không lock
    @PostMapping("/buy-unsafe/{productId}")
    public ApiResponse<String> buyUnsafe(@PathVariable Long productId) {
        boolean success = flashSaleService.buyWithoutLock(productId);
        if (success) {
            return ApiResponse.success("Đặt hàng thành công (Unsafe)! Tồn kho còn: " + flashSaleService.getStock(productId));
        } else {
            return ApiResponse.error(400, "Đặt hàng thất bại: Hết hàng!");
        }
    }

    // Mua 1 đơn lẻ có Redisson Lock
    @PostMapping("/buy-safe/{productId}")
    public ApiResponse<String> buySafe(@PathVariable Long productId) {
        boolean success = flashSaleService.buyWithRedissonLock(productId);
        if (success) {
            return ApiResponse.success("Đặt hàng thành công (Redisson Lock)! Tồn kho còn: " + flashSaleService.getStock(productId));
        } else {
            return ApiResponse.error(400, "Đặt hàng thất bại: Hết hàng!");
        }
    }

    // Giả lập 100 User bấm Mua cùng lúc KHÔNG DÙNG LOCK (Hiện tượng Race Condition / Overselling)
    @PostMapping("/simulate-race-condition/{productId}")
    public ApiResponse<FlashSaleResponse> simulateRaceCondition(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "100") int concurrentRequests
    ) {
        FlashSaleResponse response = flashSaleService.runConcurrentTest(productId, false, concurrentRequests);
        return ApiResponse.success(response);
    }

    // Giả lập 100 User bấm Mua cùng lúc CÓ REDISSON LOCK (Safe 100%)
    @PostMapping("/simulate-distributed-lock/{productId}")
    public ApiResponse<FlashSaleResponse> simulateDistributedLock(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "100") int concurrentRequests
    ) {
        FlashSaleResponse response = flashSaleService.runConcurrentTest(productId, true, concurrentRequests);
        return ApiResponse.success(response);
    }
}
