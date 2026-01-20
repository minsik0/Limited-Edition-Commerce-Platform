package com.sparta.productservice.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {

    READY("판매 준비"),
    OPEN("판매 중"),
    SOLD_OUT("품절"),
    CLOSED("판매 종료");

    private final String description;

    //READY 상태에서만 OPEN 가능
    public boolean canOpen() {
        return this == READY;
    }

    //OPEN 상태에서만 SOLD_OUT 가능
    public boolean canSoldOut() {
        return this == OPEN;
    }

    //OPEN 상태에서만 CLOSED 가능 (관리자 강제 종료)
    public boolean canClose() {
        return this == OPEN;
    }
}