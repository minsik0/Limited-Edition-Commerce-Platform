package com.sparta.orderservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionForOrderResponse {

    private UUID productId;
    private String productName;

    private UUID optionId;
    private String optionName;

    private int price;
    private int remainStock;
}
