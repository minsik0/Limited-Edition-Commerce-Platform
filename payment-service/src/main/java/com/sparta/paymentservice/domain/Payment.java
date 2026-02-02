package com.sparta.paymentservice.domain;

import com.sparta.paymentservice.global.exception.BusinessException;
import com.sparta.paymentservice.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = PROTECTED)
public class Payment {

    @Id
    @GeneratedValue
    private UUID paymentId;

    @Column(nullable = false, updatable = false)
    private UUID orderId;

    @Column(nullable = false, updatable = false)
    private UUID userId;

    @Enumerated(STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String paymentMethod;

    private String transactionId;

    private LocalDateTime approvedAt;

    private LocalDateTime failedAt;

    private LocalDateTime deletedAt;

    @Builder
    private Payment(
            UUID orderId,
            UUID userId,
            Long amount,
            String paymentMethod
    ) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.PENDING;
    }

    public void approve(String transactionId) {
        validateNotDeleted();
        validateStatus(PaymentStatus.PENDING);

        this.status = PaymentStatus.APPROVED;
        this.transactionId = transactionId;
        this.approvedAt = LocalDateTime.now();
    }

    public void cancel() {
        validateNotDeleted();
        validateStatus(PaymentStatus.APPROVED);

        this.status = PaymentStatus.CANCELED;
    }

    private void validateStatus(PaymentStatus expected) {
        if (this.status != expected) {
            throw new BusinessException(ErrorCode.INVALID_PAYMENT_STATUS);
        }
    }

    private void validateNotDeleted() {
        if (this.deletedAt != null) {
            throw new BusinessException(ErrorCode.PAYMENT_NOT_FOUND);
        }
    }
}
