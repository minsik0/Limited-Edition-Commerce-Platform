package com.sparta.orderservice.application.dto.response;

import com.sparta.orderservice.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse {

    private UUID orderId;
    private OrderStatus status;
}
