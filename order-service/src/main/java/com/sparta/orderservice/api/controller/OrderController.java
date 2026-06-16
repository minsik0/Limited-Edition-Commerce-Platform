package com.sparta.orderservice.api.controller;

import com.sparta.multi_module.common.response.ApiResponse;
import com.sparta.multi_module.common.security.DefaultAuthenticatedUser;
import com.sparta.orderservice.application.dto.request.CreateOrderRequest;
import com.sparta.orderservice.application.dto.response.CreateOrderResponse;
import com.sparta.orderservice.application.service.OrderCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Order", description = "주문 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderCommandService orderCommandService;

    @Operation(summary = "주문 생성",
            description = "한정판 상품 주문. Kafka 기반 Saga로 재고 차감 후 주문 확정/실패 처리. "
                    + "1인당 최대 구매 수량 초과 시 거부")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "주문 생성 성공 (PENDING 상태)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "구매 한도 초과 또는 유효성 검증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 상품/옵션")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(
            @Parameter(hidden = true) @AuthenticationPrincipal DefaultAuthenticatedUser principal,
            @RequestBody CreateOrderRequest request) {
        UUID userId = principal.getUserId();
        CreateOrderResponse response = orderCommandService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(summary = "주문 취소", description = "CREATED 또는 PENDING 상태의 주문만 취소 가능")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "취소 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "취소 불가 상태"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "본인 주문이 아님"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 주문")
    })
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @Parameter(hidden = true) @AuthenticationPrincipal DefaultAuthenticatedUser principal,
            @PathVariable UUID orderId) {
        UUID userId = principal.getUserId();
        orderCommandService.cancelOrder(userId, orderId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}