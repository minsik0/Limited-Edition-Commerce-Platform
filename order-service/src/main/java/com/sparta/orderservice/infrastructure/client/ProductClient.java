package com.sparta.orderservice.infrastructure.client;

import com.sparta.productservice.application.dto.response.ProductOptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "product-service", url = "${product.service.url}")
public interface ProductClient {

    @GetMapping("/internal/products/{productId}/options/{optionId}")
    ProductOptionResponse getProductOption(
            @PathVariable UUID productId,
            @PathVariable UUID optionId
    );

    @PostMapping("/internal/products/{productId}/options/{optionId}/deduct")
    void deductStock(
            @PathVariable UUID productId,
            @PathVariable UUID optionId,
            @RequestParam int quantity
    );
}

