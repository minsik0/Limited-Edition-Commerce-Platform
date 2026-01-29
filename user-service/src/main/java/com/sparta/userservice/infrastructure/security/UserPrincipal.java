package com.sparta.userservice.infrastructure.security;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserPrincipal {

    private final UUID userId;
    private final String role;

    public UserPrincipal(UUID userId, String role) {
        this.userId = userId;
        this.role = role;
    }
}
