package com.sparta.productservice.api.controller;

import com.sparta.multi_module.common.response.ApiResponse;
import com.sparta.productservice.application.dto.request.ProductOptionCreateRequest;
import com.sparta.productservice.application.dto.request.ProductOptionUpdateRequest;
import com.sparta.productservice.application.dto.response.ProductOptionResponse;
import com.sparta.productservice.application.service.ProductOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Product Option", description = "상품 옵션(사이즈/색상/재고) 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products/{productId}/options")
public class ProductOptionController {

    private final ProductOptionService productOptionService;

    @Operation(summary = "옵션 등록", description = "상품에 사이즈/색상/재고 옵션 추가")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "옵션 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 상품")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, UUID>>> create(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductOptionCreateRequest request) {
        UUID optionId = productOptionService.create(productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(Map.of("optionId", optionId)));
    }

    @Operation(summary = "재고 원자적 차감", description = "원자적 UPDATE 쿼리로 재고 차감 (동시성 안전)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "차감 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "재고 부족")
    })
    @PostMapping("/{optionId}/atomic-stock")
    public ResponseEntity<Void> decreaseStockAtomic(
            @PathVariable UUID productId,
            @PathVariable UUID optionId,
            @RequestParam int quantity) {
        productOptionService.decreaseStockAtomic(productId, optionId, quantity);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "옵션 목록 조회", description = "해당 상품의 모든 옵션 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductOptionResponse>>> getByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(ApiResponse.success(productOptionService.getByProductId(productId)));
    }

    @Operation(summary = "옵션 수정", description = "사이즈, 색상, 재고 변경")
    @PatchMapping("/{optionId}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable UUID optionId,
            @Valid @RequestBody ProductOptionUpdateRequest request) {
        productOptionService.update(optionId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "옵션 삭제", description = "주문 이력이 없는 옵션만 삭제 가능")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "주문 이력 존재")
    })
    @DeleteMapping("/{optionId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID optionId) {
        productOptionService.delete(optionId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}