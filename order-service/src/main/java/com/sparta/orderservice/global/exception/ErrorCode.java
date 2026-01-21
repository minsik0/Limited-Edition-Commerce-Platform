package com.sparta.orderservice.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //ORDER
    ORDER_NOT_FOUND("ORDER_001", "존재하지 않는 주문입니다."),
    ORDER_ACCESS_DENIED("ORDER_002", "주문에 대한 접근 권한이 없습니다."),
    ORDER_CANNOT_CANCEL("ORDER_003", "취소할 수 없는 주문 상태입니다."),
    INVALID_ORDER_STATUS("ORDER_004", "유효하지 않은 주문 상태입니다."),

    // COMMON
    INVALID_REQUEST("COMMON_001", "잘못된 요청입니다"),
    INTERNAL_SERVER_ERROR("COMMON_999", "서버 오류가 발생했습니다");

    private final String code;
    private final String message;
}
