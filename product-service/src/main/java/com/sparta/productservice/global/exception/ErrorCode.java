package com.sparta.productservice.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // PRODUCT
    PRODUCT_NOT_FOUND("PRODUCT_001", "존재하지 않는 상품입니다"),
    PRODUCT_NOT_OPEN_TIME("PRODUCT_002", "아직 오픈되지 않은 상품입니다"),
    INVALID_PRODUCT_STATE("PRODUCT_003", "상품 상태가 올바르지 않습니다"),
    PRODUCT_ALREADY_CLOSED("PRODUCT_004", "이미 종료된 상품입니다"),

    // OPTION
    OPTION_NOT_FOUND("OPTION_001", "존재하지 않는 상품 옵션입니다"),
    INSUFFICIENT_STOCK("OPTION_002", "재고가 부족합니다"),
    OPTION_HAS_ORDER("OPTION_003", "주문 이력이 있는 옵션은 삭제할 수 없습니다"),

    // COMMON
    INVALID_REQUEST("COMMON_001", "잘못된 요청입니다"),
    INTERNAL_SERVER_ERROR("COMMON_999", "서버 오류가 발생했습니다");

    private final String code;
    private final String message;
}
