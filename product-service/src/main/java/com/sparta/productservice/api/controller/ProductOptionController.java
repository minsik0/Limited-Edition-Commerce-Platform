package com.sparta.productservice.api.controller;

import com.sparta.productservice.application.dto.request.ProductOptionCreateRequest;
import com.sparta.productservice.application.dto.request.ProductOptionUpdateRequest;
import com.sparta.productservice.application.dto.response.ProductOptionResponse;
import com.sparta.productservice.application.dto.response.ProductStatusResponse;
import com.sparta.productservice.application.service.ProductOptionService;
import com.sparta.productservice.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/{productId}/options")
public class ProductOptionController {

    private final ProductOptionService productOptionService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, UUID>>> create(@PathVariable UUID productId,
                                                                 @Valid @RequestBody ProductOptionCreateRequest request) {
        UUID optionId = productOptionService.create(productId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(Map.of("optionId", optionId)));
    }

    @GetMapping
    public  ResponseEntity<ApiResponse<List<ProductOptionResponse>>> getByProduct(@PathVariable UUID productId) {

        return ResponseEntity.ok(ApiResponse.success(productOptionService.getByProductId(productId)));
    }

    @PatchMapping("/{optionId}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable UUID optionId,
                                                    @Valid @RequestBody ProductOptionUpdateRequest request) {
        productOptionService.update(optionId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID optionId) {

        productOptionService.delete(optionId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
