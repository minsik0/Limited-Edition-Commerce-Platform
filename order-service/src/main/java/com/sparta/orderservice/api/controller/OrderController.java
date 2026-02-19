package com.sparta.orderservice.api.controller;

import com.sparta.multi_module.common.response.ApiResponse;
import com.sparta.multi_module.common.security.DefaultAuthenticatedUser;
import com.sparta.orderservice.application.dto.request.CreateOrderRequest;
import com.sparta.orderservice.application.dto.response.CreateOrderResponse;
import com.sparta.orderservice.application.service.OrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderCommandService orderCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(@AuthenticationPrincipal DefaultAuthenticatedUser principal,
                                                                        @RequestBody CreateOrderRequest request) {
        UUID userId = principal.getUserId();
        CreateOrderResponse response = orderCommandService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@AuthenticationPrincipal DefaultAuthenticatedUser principal,
                                                         @PathVariable UUID orderId) {
        UUID userId = principal.getUserId();
        orderCommandService.cancelOrder(userId, orderId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
