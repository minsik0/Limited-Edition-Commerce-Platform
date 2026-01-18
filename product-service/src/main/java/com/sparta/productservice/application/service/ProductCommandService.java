package com.sparta.productservice.application.service;

import com.sparta.productservice.application.dto.request.ProductCreateRequest;
import com.sparta.productservice.application.dto.request.ProductUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ProductCommandService {

    UUID create(ProductCreateRequest request);

    void update(UUID productId, ProductUpdateRequest request);

    void open(UUID productId);

    void close(UUID productId);


}
