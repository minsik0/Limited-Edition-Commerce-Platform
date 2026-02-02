package com.sparta.paymentservice.application.dto.response;


import com.sparta.paymentservice.domain.Payment;
import com.sparta.paymentservice.domain.PaymentStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class PaymentResponse {

    private final UUID paymentId;
    private final UUID orderId;
    private final PaymentStatus status;
    private final Long amount;
    private final String paymentMethod;
    private final LocalDateTime approvedAt;


    public PaymentResponse(UUID paymentId, UUID orderId, PaymentStatus status, Long amount, String paymentMethod, LocalDateTime approvedAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.status = status;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.approvedAt = approvedAt;
    }

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getApprovedAt()
        );
    }
}
