package com.sparta.productservice.api.controller;

import com.sparta.multi_module.common.response.ApiResponse;
import com.sparta.productservice.application.dto.request.ProductCreateRequest;
import com.sparta.productservice.application.dto.request.ProductCursorRequest;
import com.sparta.productservice.application.dto.request.ProductUpdateRequest;
import com.sparta.productservice.application.dto.response.CursorPageResponse;
import com.sparta.productservice.application.dto.response.ProductResponse;
import com.sparta.productservice.application.dto.response.ProductStatusResponse;
import com.sparta.productservice.application.dto.response.ProductSummaryResponse;
import com.sparta.productservice.application.service.ProductCommandService;
import com.sparta.productservice.application.service.ProductQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Product", description = "한정판 상품 관리 API")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    @Operation(summary = "상품 등록", description = "MASTER 권한 필요. 한정판 상품 생성")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "상품 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효성 검증 실패")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, UUID>>> create(@Valid @RequestBody ProductCreateRequest request) {
        UUID productId = productCommandService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(Map.of("productId", productId)));
    }

    @Operation(summary = "상품 목록 조회 (Offset 페이징)", description = "Spring Data Pageable 기반 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductSummaryResponse>>> getPage(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(productQueryService.getPage(pageable)));
    }

    @Operation(summary = "상품 상세 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 상품")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> get(@PathVariable UUID productId) {
        return ResponseEntity.ok(ApiResponse.success(productQueryService.get(productId)));
    }

    @Operation(summary = "상품 목록 조회 (커서 페이징)",
            description = "openAt DESC, productId DESC 기준 커서 기반 페이징. Redis 캐시 적용 (TTL 3분)")
    @GetMapping("/cursor")
    public ResponseEntity<ApiResponse<CursorPageResponse<ProductSummaryResponse>>> getCursorPage(
            @Parameter(description = "이전 페이지 마지막 상품 ID") @RequestParam(required = false) UUID cursorId,
            @Parameter(description = "페이지 크기 (기본 20)") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "이전 페이지 마지막 상품 openAt") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursorOpenAt) {

        ProductCursorRequest request = new ProductCursorRequest(cursorId, size, cursorOpenAt);
        return ResponseEntity.ok(ApiResponse.success(productQueryService.getCursorPage(request)));
    }

    @Operation(summary = "상품 정보 수정", description = "MASTER 권한 필요")
    @PatchMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductStatusResponse>> update(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(productCommandService.update(productId, request)));
    }

    @Operation(summary = "상품 오픈", description = "MASTER 권한 필요. 상품 상태를 OPEN으로 변경")
    @PatchMapping("/{productId}/open")
    public ResponseEntity<ApiResponse<ProductStatusResponse>> open(@PathVariable UUID productId) {
        return ResponseEntity.ok(ApiResponse.success(productCommandService.open(productId)));
    }

    @Operation(summary = "상품 종료", description = "MASTER 권한 필요. 상품 상태를 CLOSED로 변경, 옵션 소프트 삭제")
    @PatchMapping("/{productId}/close")
    public ResponseEntity<ApiResponse<ProductStatusResponse>> close(@PathVariable UUID productId) {
        return ResponseEntity.ok(ApiResponse.success(productCommandService.close(productId)));
    }
}
