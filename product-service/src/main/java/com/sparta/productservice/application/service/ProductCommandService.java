package com.sparta.productservice.application.service;

import com.sparta.productservice.application.dto.request.ProductCreateRequest;
import com.sparta.productservice.application.dto.request.ProductUpdateRequest;
import com.sparta.productservice.application.dto.response.ProductStatusResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ProductCommandService {

    UUID create(ProductCreateRequest request);

    ProductStatusResponse update(UUID productId, ProductUpdateRequest request);

    ProductStatusResponse open(UUID productId);

    ProductStatusResponse close(UUID productId);


}
