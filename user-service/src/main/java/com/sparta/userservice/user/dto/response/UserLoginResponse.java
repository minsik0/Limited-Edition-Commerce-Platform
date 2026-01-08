package com.sparta.userservice.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponse {
    private String accessToken;
    private Long userId;
    private String role;
}
