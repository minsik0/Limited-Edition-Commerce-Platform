package com.sparta.productservice.application.service;

import com.sparta.productservice.application.dto.response.ProductResponse;
import com.sparta.productservice.application.dto.response.ProductSummaryResponse;
import com.sparta.productservice.domain.product.Product;
import com.sparta.productservice.global.exception.BusinessException;
import com.sparta.productservice.global.exception.ErrorCode;
import com.sparta.productservice.infrastructure.persistence.ProductOptionRepository;
import com.sparta.productservice.infrastructure.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    @Override
    public Page<ProductSummaryResponse> getPage(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductSummaryResponse::from);
    }

    @Override
    public ProductResponse get(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        int currentQuantity = productOptionRepository.sumRemainStockByProductId(productId);

        return ProductResponse.of(product, currentQuantity);
    }

}
