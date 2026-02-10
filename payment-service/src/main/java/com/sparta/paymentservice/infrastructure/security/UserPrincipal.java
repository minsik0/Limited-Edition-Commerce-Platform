package com.sparta.paymentservice.infrastructure.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserPrincipal {

    private final UUID userId;
    private final String role;

}
