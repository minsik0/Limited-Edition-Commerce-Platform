package com.sparta.orderservice.domain.order;

public enum OrderStatus {
    PENDING,    // 주문 접수, 재고 차감 대기
    CREATED,    // 재고 확보 완료, 주문 확정
    PAID,       // 결제 완료
    CANCELED,   // 주문 취소
    FAILED;     // 재고 차감 실패

    public boolean isCancelable() {
        return this == PENDING || this == CREATED;
    }
}
