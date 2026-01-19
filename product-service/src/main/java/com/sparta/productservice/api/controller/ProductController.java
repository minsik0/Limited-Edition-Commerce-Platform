package com.sparta.productservice.api.controller;

import com.sparta.productservice.application.dto.request.ProductCreateRequest;
import com.sparta.productservice.application.dto.request.ProductUpdateRequest;
import com.sparta.productservice.application.dto.response.ProductResponse;
import com.sparta.productservice.application.dto.response.ProductStatusResponse;
import com.sparta.productservice.application.dto.response.ProductSummaryResponse;
import com.sparta.productservice.application.service.ProductCommandService;
import com.sparta.productservice.application.service.ProductQueryService;
import com.sparta.productservice.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, UUID>>> create(@Valid @RequestBody ProductCreateRequest request) {
        UUID productId = productCommandService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(Map.of("productId", productId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductSummaryResponse>>> getPage(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(productQueryService.getPage(pageable)));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> get(@PathVariable UUID productId) {
        return ResponseEntity.ok(ApiResponse.success(productQueryService.get(productId)));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductStatusResponse>> update(@PathVariable UUID productId,
                                                                     @RequestBody ProductUpdateRequest request) {

        return ResponseEntity.ok(ApiResponse.success(productCommandService.update(productId, request)));
    }

    @PostMapping("/{productId}/open")
    public ResponseEntity<ApiResponse<ProductStatusResponse>> open(@Valid @PathVariable UUID productId) {

        return ResponseEntity.ok(ApiResponse.success(productCommandService.open(productId)));
    }

    @PostMapping("/{productId}/close")
    public ResponseEntity<ApiResponse<ProductStatusResponse>> close(@Valid @PathVariable UUID productId) {

        return ResponseEntity.ok(ApiResponse.success(productCommandService.close(productId)));
    }



}
