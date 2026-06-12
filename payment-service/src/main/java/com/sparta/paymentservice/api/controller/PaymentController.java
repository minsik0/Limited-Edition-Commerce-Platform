package com.sparta.paymentservice.api.controller;

import com.sparta.multi_module.common.response.ApiResponse;
import com.sparta.multi_module.common.security.DefaultAuthenticatedUser;
import com.sparta.paymentservice.application.dto.request.PaymentCreateRequest;
import com.sparta.paymentservice.application.dto.response.PaymentResponse;
import com.sparta.paymentservice.application.service.PaymentService;
import com.sparta.paymentservice.domain.Payment;
import com.sparta.paymentservice.domain.PaymentStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "Payment", description = "결제 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 생성",
            description = "주문에 대한 결제 생성. orderId 기반 멱등성 보장 (중복 결제 방지)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "결제 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효성 검증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 결제된 주문")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @RequestBody PaymentCreateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal DefaultAuthenticatedUser principal) {
        UUID userId = principal.getUserId();
        Payment payment = paymentService.createPayment(
                userId,
                request.getOrderId(),
                request.getPaymentMethod()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(PaymentResponse.from(payment)));
    }

    @Operation(summary = "결제 내역 조회", description = "본인의 결제 내역 목록. 상태별 필터링 가능")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPayments(
            @Parameter(description = "결제 상태 필터 (APPROVED, CANCELED 등)")
            @RequestParam(required = false) PaymentStatus status,
            @Parameter(hidden = true) @AuthenticationPrincipal DefaultAuthenticatedUser principal) {
        UUID userId = principal.getUserId();
        List<PaymentResponse> responses = paymentService.getPayments(userId, status)
                .stream()
                .map(PaymentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "결제 상세 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "결제 정보 없음")
    })
    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @PathVariable UUID paymentId,
            @Parameter(hidden = true) @AuthenticationPrincipal DefaultAuthenticatedUser principal) {
        UUID userId = principal.getUserId();
        Payment payment = paymentService.getPayment(paymentId, userId);
        return ResponseEntity.ok(ApiResponse.success(PaymentResponse.from(payment)));
    }

    @Operation(summary = "결제 취소")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "취소 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "취소 불가 상태")
    })
    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelPayment(
            @PathVariable UUID paymentId,
            @Parameter(hidden = true) @AuthenticationPrincipal DefaultAuthenticatedUser principal) {
        UUID userId = principal.getUserId();
        paymentService.cancelPayment(paymentId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "결제 삭제", description = "소프트 삭제 처리")
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<Void>> deletePayment(
            @PathVariable UUID paymentId,
            @Parameter(hidden = true) @AuthenticationPrincipal DefaultAuthenticatedUser principal) {
        UUID userId = principal.getUserId();
        paymentService.deletePayment(paymentId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}