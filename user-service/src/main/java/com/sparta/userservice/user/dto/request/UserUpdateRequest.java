package com.sparta.userservice.user.dto.request;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private String name;
    private String password;
}
