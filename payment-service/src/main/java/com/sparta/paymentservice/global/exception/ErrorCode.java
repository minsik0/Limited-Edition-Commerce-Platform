package com.sparta.paymentservice.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // PAYMENT
    PAYMENT_NOT_FOUND("PAYMENT_001", "결제 정보를 찾을 수 없습니다"),
    PAYMENT_ALREADY_APPROVED("PAYMENT_002", "이미 승인된 결제입니다"),
    PAYMENT_FAILED("PAYMENT_003", "결제에 실패했습니다"),
    INVALID_PAYMENT_STATUS("PAYMENT_004", "결제 상태가 올바르지 않습니다"),
    PAYMENT_ALREADY_HIDDEN("PAYMENT_005", "이미 비활성화된 결제입니다"),

    // ORDER
    ORDER_NOT_FOUND("ORDER_001", "주문 정보를 찾을 수 없습니다"),

    // COMMON
    INVALID_REQUEST("COMMON_001", "잘못된 요청입니다"),
    INTERNAL_SERVER_ERROR("COMMON_999", "서버 오류가 발생했습니다");

    private final String code;
    private final String message;
}
