package com.sparta.orderservice.application.dto.response;

import com.sparta.orderservice.domain.order.OrderItem;
import com.sparta.orderservice.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private UUID orderId;
    private OrderStatus orderStatus;
    private Integer totalPrice;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
}
