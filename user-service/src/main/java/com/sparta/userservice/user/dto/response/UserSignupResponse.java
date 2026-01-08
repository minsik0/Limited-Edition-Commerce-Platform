package com.sparta.userservice.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignupResponse {

    private Long userId;
    private String email;
    private String name;
    private String role;
}
