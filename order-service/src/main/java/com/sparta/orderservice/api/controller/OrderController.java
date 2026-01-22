package com.sparta.orderservice.api.controller;

import com.sparta.orderservice.application.dto.request.CreateOrderRequest;
import com.sparta.orderservice.application.dto.response.CreateOrderResponse;
import com.sparta.orderservice.application.service.OrderCommandService;
import com.sparta.orderservice.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderCommandService orderCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(@RequestHeader("X-User-Id") UUID userId,
                                                                        @RequestBody CreateOrderRequest request) {
        CreateOrderResponse response = orderCommandService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PatchMapping("/{orderId}/cancle")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@RequestHeader("X-User-Id") UUID userId,
                                                         @PathVariable UUID orderId) {
        orderCommandService.cancelOrder(userId, orderId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
