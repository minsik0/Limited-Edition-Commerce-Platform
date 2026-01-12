package com.sparta.userservice.application.dto.request;

import lombok.Getter;

@Getter
public class UserSignupRequest {
    private String email;
    private String password;
    private String name;
}
