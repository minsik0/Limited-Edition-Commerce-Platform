package com.sparta.productservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ProductCreateRequest {

    @NotBlank(message = "상품명은 필수입니다.")
    private String name;

    @Positive(message = "가격은 0보다 커야 합니다.")
    private int price;

    @Positive(message = "1인당 구매 제한 수량은 1 이상이어야 합니다.")
    private int maxPurchasePerUser;

    @NotNull(message = "오픈 시간은 필수입니다.")
    private LocalDateTime openAt;
}
