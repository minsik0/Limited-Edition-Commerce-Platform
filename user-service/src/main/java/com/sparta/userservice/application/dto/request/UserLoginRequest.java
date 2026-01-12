package com.sparta.userservice.application.dto.request;

import lombok.Getter;

@Getter
public class UserLoginRequest {
    private String email;
    private String password;
}
