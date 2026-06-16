package com.sparta.productservice.application.dto.response;

import com.sparta.productservice.domain.product.Product;
import com.sparta.productservice.domain.product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryResponse {

    private UUID productId;
    private String name;
    private ProductStatus status;
    private LocalDateTime openAt;

    public static ProductSummaryResponse from(Product product) {
        return new ProductSummaryResponse(
                product.getProductId(),
                product.getName(),
                product.getStatus(),
                product.getOpenAt()
        );
    }
}
