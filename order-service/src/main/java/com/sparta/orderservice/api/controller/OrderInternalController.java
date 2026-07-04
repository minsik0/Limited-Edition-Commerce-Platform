package com.sparta.orderservice.api.controller;

import com.sparta.multi_module.common.exception.BusinessException;
import com.sparta.multi_module.common.exception.ErrorCode;
import com.sparta.orderservice.application.service.OrderCommandService;
import com.sparta.orderservice.application.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/orders")
public class OrderInternalController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    @Value("${internal.secret}")
    private String internalSecret;

    // 헤더 검증 공통 메서드
    private void validateInternalCall(String internalCall) {
        if (internalSecret == null || internalSecret.isBlank() || !internalSecret.equals(internalCall)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
    }

    @PostMapping("/{orderId}/mark-paid")
    public void markOrderAsPaid(@PathVariable UUID orderId,
                                @RequestHeader(value = "X-Internal-Call", required = false)
                                String internalCall) {
        validateInternalCall(internalCall);
        orderCommandService.markOrderAsPaid(orderId);
    }

    @GetMapping("/{orderId}/amount")
    public Long getOrderAmount(@PathVariable UUID orderId,
                               @RequestHeader(value = "X-Internal-Call", required = false)
                               String internalCall) {
        validateInternalCall(internalCall);
        return orderQueryService.getOrderAmount(orderId);
    }
}

