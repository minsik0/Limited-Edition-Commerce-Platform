package com.sparta.orderservice.api.controller;

import com.sparta.multi_module.common.response.ApiResponse;
import com.sparta.multi_module.common.security.AuthenticatedUser;
import com.sparta.multi_module.common.security.DefaultAuthenticatedUser;
import com.sparta.orderservice.application.dto.response.OrderResponse;
import com.sparta.orderservice.application.dto.response.OrderSummaryResponse;
import com.sparta.orderservice.application.service.OrderQueryService;
import com.sparta.orderservice.domain.order.OrderStatus;
import com.sparta.orderservice.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderQueryController {

    private final OrderQueryService orderQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderSummaryResponse>>> getOrders(@AuthenticationPrincipal DefaultAuthenticatedUser principal,
                                                                             @RequestParam(required = false) OrderStatus status) {
        UUID userId = principal.getUserId();
        List<OrderSummaryResponse> responses = orderQueryService.getOrders(userId, status);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@AuthenticationPrincipal DefaultAuthenticatedUser principal,
                                                               @PathVariable UUID orderId) {
        UUID userId = principal.getUserId();
        OrderResponse response = orderQueryService.getOrder(userId, orderId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
