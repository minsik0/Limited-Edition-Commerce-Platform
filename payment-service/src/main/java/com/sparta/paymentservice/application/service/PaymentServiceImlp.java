package com.sparta.paymentservice.application.service;

import com.sparta.multi_module.common.exception.BusinessException;
import com.sparta.multi_module.common.exception.ErrorCode;
import com.sparta.paymentservice.domain.Payment;
import com.sparta.paymentservice.domain.PaymentStatus;
import com.sparta.paymentservice.inflastructure.PaymentRepository;
import com.sparta.paymentservice.inflastructure.client.OrderClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImlp implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    @Override
    public Payment createPayment(UUID userId, UUID orderId, String paymentMethod) {

        Long orderAmount = orderClient.getOrderAmount(orderId, "internal-secret");

        Payment payment = Payment.builder()
                .userId(userId)
                .orderId(orderId)
                .paymentMethod(paymentMethod)
                .amount(orderAmount)
                .build();

        paymentRepository.save(payment);

        payment.approve(generateMockTransactionId());
        orderClient.markOrderAsPaid(orderId, "internal-secret");

        return payment;
    }

    @Override
    public List<Payment> getPayments(UUID userId, PaymentStatus status) {
        if(status == null){
            return paymentRepository.findAllByUserIdAndDeletedAtIsNull(userId);
        }

        return paymentRepository.findAllByUserIdAndStatusAndDeletedAtIsNull(userId, status);
    }

    @Override
    public Payment getPayment(UUID paymentId, UUID userId) {
        Payment payment = paymentRepository.findByIdAndDeletedAtIsNull(paymentId)
                .orElseThrow(()-> new BusinessException(ErrorCode.PAYMENT_NOT_FOUND));

        validateOwner(payment, userId);
        return payment;
    }

    @Override
    public void cancelPayment(UUID userId, UUID paymentId) {
        Payment payment = getPayment(paymentId, userId);
        payment.cancel();
    }

    @Override
    public void deletePayment(UUID paymentId, UUID userId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PAYMENT_NOT_FOUND));
        validateOwner(payment, userId);
        payment.hide();
    }


    private void validateOwner(Payment payment, UUID userId) {
        if(!payment.getUserId().equals(userId)){
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
    }

    private String generateMockTransactionId(){
        return "MOCK-" + UUID.randomUUID();
    }
}
