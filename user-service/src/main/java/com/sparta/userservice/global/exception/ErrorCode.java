package com.sparta.userservice.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("USER_001", "존재하지 않는 사용자 입니다"),
    INVALID_PASSWORD("USER_002", "비밀번호가 일치하지 않습니다"),
    DUPLICATE_EMAIL("USER_003", "이미 존재하는 이메일입니다"),

    INVALID_REQUEST("COMMON_001", "잘못된 요청입니다"),
    INTERNAL_SERVER_ERROR("COMMON_999", "서버 오류가 발생했습니다"),

    EXPIRED_TOKEN("JWT_001", "JWT 토큰이 만료되었습니다."),
    INVALID_TOKEN("JWT_002", "JWT 토큰이 유효하지 않습니다."),
    INVALID_SIGNATURE("JWT_003", "JWT 서명이 유효하지 않습니다."),
    UNSUPPORTED_TOKEN("JWT_004", "지원하지 않는 JWT 토큰입니다."),
    MALFORMED_TOKEN("JWT_005", "JWT 토큰 형식이 올바르지 않습니다."),
    UNKNOWN_ERROR("JWT_006", "JWT 처리 중 알 수 없는 오류가 발생했습니다.");

    private final String code;
    private final String message;
}
