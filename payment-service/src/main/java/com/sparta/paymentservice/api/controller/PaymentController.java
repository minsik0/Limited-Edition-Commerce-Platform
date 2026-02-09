package com.sparta.paymentservice.api.controller;

import com.sparta.multi_module.common.response.ApiResponse;
import com.sparta.paymentservice.application.dto.request.PaymentCreateRequest;
import com.sparta.paymentservice.application.dto.response.PaymentResponse;
import com.sparta.paymentservice.application.service.PaymentService;
import com.sparta.paymentservice.domain.Payment;
import com.sparta.paymentservice.domain.PaymentStatus;
import com.sparta.paymentservice.inflastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(@RequestBody PaymentCreateRequest request,
                                                                      @AuthenticationPrincipal UserPrincipal principal) {
        UUID userId = principal.getUserId();

        Payment payment = paymentService.createPayment(
                userId,
                request.getOrderId(),
                request.getPaymentMethod()
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(PaymentResponse.from(payment)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPayments(@RequestParam(required = false) PaymentStatus status,
                                                                           @AuthenticationPrincipal UserPrincipal principal) {
        UUID userId = principal.getUserId();

        List<PaymentResponse> responses = paymentService.getPayments(userId, status)
                .stream()
                .map(PaymentResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(@PathVariable UUID paymentId,
                                                                   @AuthenticationPrincipal UserPrincipal principal) {
        UUID userId = principal.getUserId();
        Payment payment = paymentService.getPayment(paymentId, userId);
        return ResponseEntity.ok(ApiResponse.success(PaymentResponse.from(payment)));
    }

    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelPayment(@PathVariable UUID paymentId,
                                                           @AuthenticationPrincipal UserPrincipal principal) {
        UUID userId = principal.getUserId();
        paymentService.cancelPayment(paymentId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<Void>> deletePayment(@PathVariable UUID paymentId,
                                                           @AuthenticationPrincipal UserPrincipal principal) {
        UUID userId = principal.getUserId();
        paymentService.deletePayment(paymentId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
