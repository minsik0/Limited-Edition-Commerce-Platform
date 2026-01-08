package com.sparta.userservice.user.dto.request;

import lombok.Getter;

@Getter
public class UserSignupRequest {
    private String email;
    private String password;
    private String name;
}
