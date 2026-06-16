package com.sparta.productservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionCreateRequest {

    @NotBlank(message = "사이즈는 필수입니다.")
    private String size;

    @NotBlank(message = "컬러는 필수입니다.")
    private String color;

    @PositiveOrZero(message = "재고는 0 이상이어야 합니다.")
    private int stock;
}
