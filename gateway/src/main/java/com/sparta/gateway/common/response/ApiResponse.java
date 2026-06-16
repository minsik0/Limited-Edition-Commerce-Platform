package com.sparta.gateway.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private ErrorResponse error;

    public static ApiResponse<Void> fail(
            String code,
            String message
    ) {
        return new ApiResponse<>(
                false,
                null,
                new ErrorResponse(code, message)
        );
    }
}