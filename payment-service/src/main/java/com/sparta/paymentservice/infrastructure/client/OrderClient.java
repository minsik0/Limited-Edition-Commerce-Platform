package com.sparta.paymentservice.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "order-service", url = "${order.service.url}")
public interface OrderClient {

    @GetMapping("/internal/orders/{orderId}/amount")
    Long getOrderAmount(@PathVariable UUID orderId, @RequestHeader("X-Internal-Call") String internalCall);

    @PostMapping("/internal/orders/{orderId}/mark-paid")
    void markOrderAsPaid(@PathVariable UUID orderId, @RequestHeader("X-Internal-Call") String internalCall);
}