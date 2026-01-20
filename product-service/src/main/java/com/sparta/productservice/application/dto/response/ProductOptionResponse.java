package com.sparta.productservice.application.dto.response;

import com.sparta.productservice.domain.option.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductOptionResponse {

    private UUID optionId;
    private String size;
    private String color;
    private int remainStock;

    public static ProductOptionResponse from(ProductOption option) {
        return new ProductOptionResponse(
                option.getOptionId(),
                option.getSize(),
                option.getColor(),
                option.getRemainStock()
        );
    }
}
