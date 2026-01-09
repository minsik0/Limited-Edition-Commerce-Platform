package com.sparta.userservice.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserUpdateResponse {
    private UUID userId;
    private String name;
    private LocalDateTime updatedAt;
}
