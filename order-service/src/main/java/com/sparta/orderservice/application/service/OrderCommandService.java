package com.sparta.orderservice.application.service;

import com.sparta.orderservice.application.dto.request.CreateOrderRequest;
import com.sparta.orderservice.application.dto.response.CreateOrderResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface OrderCommandService {
    CreateOrderResponse create(UUID userId, CreateOrderRequest request);
}
