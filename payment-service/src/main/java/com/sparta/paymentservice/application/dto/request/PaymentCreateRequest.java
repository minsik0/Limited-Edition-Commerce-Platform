package com.sparta.paymentservice.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PaymentCreateRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private String paymentMethod;
}
