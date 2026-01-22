package com.sparta.orderservice.application.dto.response;

import com.sparta.orderservice.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryResponse {

    private UUID orderId;
    private OrderStatus status;
    private Integer totalPrice;
    private LocalDateTime createdAt;

}
