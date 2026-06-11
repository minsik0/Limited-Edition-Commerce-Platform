package com.sparta.productservice.application.dto.response;

import com.sparta.productservice.domain.option.ProductOption;
import com.sparta.productservice.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductOptionForOrderResponse {

    private UUID productId;
    private String productName;
    private UUID optionId;
    private String optionName;
    private int price;
    private int remainStock;
    private int maxPurchasePerUser;

    public static ProductOptionForOrderResponse from(Product product, ProductOption option) {
        return new ProductOptionForOrderResponse(
                product.getProductId(),
                product.getName(),
                option.getOptionId(),
                option.getSize() + " / " + option.getColor(),
                product.getPrice(),
                option.getRemainStock(),
                product.getMaxPurchasePerUser()
        );
    }
}