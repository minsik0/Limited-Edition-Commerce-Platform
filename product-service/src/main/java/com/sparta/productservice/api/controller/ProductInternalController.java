package com.sparta.productservice.api.controller;

import com.sparta.productservice.application.dto.response.ProductOptionForOrderResponse;
import com.sparta.productservice.application.service.ProductOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/products")
public class ProductInternalController {

    private final ProductOptionService productOptionService;

    @GetMapping("/{productId}/options/{optionId}")
    public ProductOptionForOrderResponse getProductOption(@PathVariable UUID productId, @PathVariable UUID optionId) {
        return productOptionService.getOptionForOrder(productId, optionId);
    }
}
