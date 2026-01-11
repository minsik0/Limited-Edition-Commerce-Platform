package com.sparta.userservice.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("USER_001", "존재하지 않는 사용자 입니다"),
    INVALID_PASSWORD("USER_002", "비밀번호가 일치하지 않습니다"),
    DUPLICATE_EMAIL("USER_003", "이미 존재하는 이메일입니다"),

    INVALID_REQUEST("COMMON_001", "잘못된 요청입니다"),
    INTERNAL_SERVER_ERROR("COMMON_999", "서버 오류가 발생했습니다");

    private final String code;
    private final String message;
}
