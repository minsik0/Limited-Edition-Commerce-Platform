package com.sparta.orderservice.api.controller;

import com.sparta.orderservice.application.service.OrderCommandService;
import com.sparta.orderservice.application.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/orders")
public class OrderInternalController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    @PostMapping("/{orderId}/mark-paid")
    public void markOrderAsPaid(@PathVariable UUID orderId) {
        orderCommandService.markOrderAsPaid(orderId);
    }

    @GetMapping("/{orderId}/amount")
    public Long getOrderAmount(@PathVariable UUID orderId) {
        return orderQueryService.getOrderAmount(orderId);
    }
}
