package com.sparta.productservice.application.dto.response;

import com.sparta.productservice.domain.product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductStatusResponse {

    private UUID productId;
    private ProductStatus status;
}
