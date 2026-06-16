package com.sparta.paymentservice.infrastructure;

import com.sparta.paymentservice.domain.Payment;
import com.sparta.paymentservice.domain.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByPaymentIdAndDeletedAtIsNull(UUID paymentId);

    List<Payment> findAllByUserIdAndDeletedAtIsNull(UUID userId);

    List<Payment> findAllByUserIdAndStatusAndDeletedAtIsNull(UUID userId, PaymentStatus status);

    Optional<Payment> findByOrderId(UUID orderId);
}
