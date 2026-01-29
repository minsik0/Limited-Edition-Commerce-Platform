package com.sparta.paymentservice.global.response;


import com.sparta.paymentservice.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<?> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, null,
                new ErrorResponse(errorCode.getCode(), errorCode.getMessage())
        );
    }
}
