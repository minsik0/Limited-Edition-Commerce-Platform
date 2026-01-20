package com.sparta.productservice.application.dto.response;

import com.sparta.productservice.domain.product.Product;
import com.sparta.productservice.domain.product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductResponse {

    private UUID productId;
    private String name;
    private int price;
    private int currentQuantity;
    private int maxPurchasePerUser;
    private ProductStatus status;
    private LocalDateTime openAt;

    public static ProductResponse of(Product product, int currentQuantity) {
        return new ProductResponse(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                currentQuantity,
                product.getMaxPurchasePerUser(),
                product.getStatus(),
                product.getOpenAt()
        );
    }
}