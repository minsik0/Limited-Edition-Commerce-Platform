package com.sparta.productservice.api.controller;

import com.sparta.productservice.application.dto.response.ProductOptionForOrderResponse;
import com.sparta.productservice.application.service.ProductOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{productId}/options/{optionId}/deduct")
    public void deductStockWithRetry(@PathVariable UUID productId, @PathVariable UUID optionId, @RequestParam int quantity) {
        productOptionService.deductStockWithRetry(productId, optionId, quantity);
    }
}
