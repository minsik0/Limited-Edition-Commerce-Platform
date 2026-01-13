package com.sparta.userservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserSignupResponse {

    private UUID userId;
    private String email;
    private String name;
    private String role;
}
