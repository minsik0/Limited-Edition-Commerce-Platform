package com.sparta.orderservice.api.controller;

import com.sparta.orderservice.application.dto.response.OrderResponse;
import com.sparta.orderservice.application.dto.response.OrderSummaryResponse;
import com.sparta.orderservice.application.service.OrderQueryService;
import com.sparta.orderservice.domain.order.OrderStatus;
import com.sparta.orderservice.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderQueryController {

    private final OrderQueryService orderQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderSummaryResponse>>> getOrders(@RequestHeader("X-User-Id") UUID userId,
                                                                             @RequestParam(required = false) OrderStatus status) {
        List<OrderSummaryResponse> responses = orderQueryService.getOrders(userId, status);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@RequestHeader("X-User-Id") UUID userId,
                                                               @PathVariable UUID orderId) {
        OrderResponse response = orderQueryService.getOrder(userId, orderId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
