package com.sparta.orderservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

    private UUID productId;
    private String productName;
    private UUID orderId;
    private String optionName;
    private Integer price;
    private Integer quantity;
}
