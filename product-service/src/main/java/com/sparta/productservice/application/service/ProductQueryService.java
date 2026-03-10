package com.sparta.productservice.application.service;

import com.sparta.productservice.application.dto.request.ProductCursorRequest;
import com.sparta.productservice.application.dto.response.CursorPageResponse;
import com.sparta.productservice.application.dto.response.ProductResponse;
import com.sparta.productservice.application.dto.response.ProductSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public interface ProductQueryService {

    Page<ProductSummaryResponse> getPage(Pageable pageable);

    ProductResponse get(UUID productId);

    CursorPageResponse<ProductSummaryResponse> getCursorPage(ProductCursorRequest request);
}
