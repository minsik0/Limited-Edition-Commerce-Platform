package com.sparta.paymentservice.domain;

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
}
