package com.sparta.userservice.application.dto.request;

import lombok.Getter;

@Getter
public class UserSignupRequest {
    @jakarta.validation.constraints.Email(message = "유효한 이메일 형식이 아닙니다.")
    @jakarta.validation.constraints.NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;
    @jakarta.validation.constraints.NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
    @jakarta.validation.constraints.NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;
}
