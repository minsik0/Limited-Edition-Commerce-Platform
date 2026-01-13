package com.sparta.userservice.application.dto.request;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private String name;
    private String password;
}
