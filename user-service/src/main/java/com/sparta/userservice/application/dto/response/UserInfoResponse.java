package com.sparta.userservice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserInfoResponse {
    private UUID userId;
    private String email;
    private String name;
    private String role;
    private LocalDateTime createdAt;
}
