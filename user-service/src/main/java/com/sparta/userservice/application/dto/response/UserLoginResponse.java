package com.sparta.userservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserLoginResponse {
    private String accessToken;
    private UUID userId;
    private String role;
}
