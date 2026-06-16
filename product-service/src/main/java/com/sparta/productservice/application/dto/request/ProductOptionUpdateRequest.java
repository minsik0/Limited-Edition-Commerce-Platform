package com.sparta.productservice.application.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionUpdateRequest {

    @PositiveOrZero(message = "재고는 0 이상이어야 합니다.")
    private Integer stock;

    private String size;
    private String color;
}