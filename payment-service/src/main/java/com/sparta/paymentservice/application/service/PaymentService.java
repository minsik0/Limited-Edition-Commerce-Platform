package com.sparta.paymentservice.application.service;

import com.sparta.paymentservice.domain.Payment;
import com.sparta.paymentservice.domain.PaymentStatus;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    Payment createPayment(UUID userId, UUID orderId, String paymentMethod);

    Payment getPayment(UUID paymentId, UUID userId);

    List<Payment> getPayments(UUID userId, PaymentStatus status);

    void cancelPayment(UUID paymentId, UUID userId);

}
