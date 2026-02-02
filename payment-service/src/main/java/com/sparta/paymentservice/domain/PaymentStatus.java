package com.sparta.paymentservice.domain;

public enum PaymentStatus {
    PENDING,    // 결제 생성됨
    APPROVED,   // 결제 성공
    FAILED,     // 결제 실패
    CANCELED    // 결제 취소 (환불)
}
