package com.sparta.orderservice.application.service;

import com.sparta.orderservice.application.dto.response.OrderResponse;
import com.sparta.orderservice.application.dto.response.OrderSummaryResponse;
import com.sparta.orderservice.domain.order.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface OrderQueryService {
    List<OrderSummaryResponse> getOrders(UUID userId, OrderStatus status);

    OrderResponse getOrder(UUID userId, UUID orderId);
}
