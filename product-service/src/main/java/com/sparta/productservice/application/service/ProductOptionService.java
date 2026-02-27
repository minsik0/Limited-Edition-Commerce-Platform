package com.sparta.productservice.application.service;

import com.sparta.productservice.application.dto.request.ProductOptionCreateRequest;
import com.sparta.productservice.application.dto.request.ProductOptionUpdateRequest;
import com.sparta.productservice.application.dto.response.ProductOptionForOrderResponse;
import com.sparta.productservice.application.dto.response.ProductOptionResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductOptionService {

    UUID create(UUID productId, ProductOptionCreateRequest request);

    void update(UUID optionId, ProductOptionUpdateRequest request);

    void delete(UUID optionId);

    List<ProductOptionResponse> getByProductId(UUID productId);

    ProductOptionForOrderResponse getOptionForOrder(UUID productId, UUID optionId);

    void deductStockWithRetry(UUID productId, UUID optionId, int quantity);
}
