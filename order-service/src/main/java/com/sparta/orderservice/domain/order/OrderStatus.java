package com.sparta.orderservice.domain.order;

public enum OrderStatus {
    CREATED,    // 주문 생성 직후 (결제 전)
    PAID,       // 결제 완료
    CANCELED;   // 주문 취소

    public boolean isCancelable() {
        return this == CREATED;
    }
}
