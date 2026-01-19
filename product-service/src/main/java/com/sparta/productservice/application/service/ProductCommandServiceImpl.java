package com.sparta.productservice.application.service;


import com.sparta.productservice.application.dto.request.ProductCreateRequest;
import com.sparta.productservice.application.dto.request.ProductUpdateRequest;
import com.sparta.productservice.application.dto.response.ProductStatusResponse;
import com.sparta.productservice.domain.product.Product;
import com.sparta.productservice.domain.product.ProductStatus;
import com.sparta.productservice.global.exception.BusinessException;
import com.sparta.productservice.global.exception.ErrorCode;
import com.sparta.productservice.infrastructure.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepository productRepository;

    @Override
    public UUID create(ProductCreateRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .maxPurchasePerUser(request.getMaxPurchasePerUser())
                .openAt(request.getOpenAt())
                .status(ProductStatus.READY)
                .build();

        productRepository.save(product);
        return product.getProductId();
    }

    @Override
    public ProductStatusResponse update(UUID productId, ProductUpdateRequest request) {
        Product product = findProduct(productId);

        product.updateInfo(
                request.getName(),
                request.getPrice(),
                request.getMaxPurchasePerUser(),
                request.getOpenAt()
        );

        return new ProductStatusResponse(productId, product.getStatus());
    }

    @Override
    public ProductStatusResponse open(UUID productId) {
        Product product = findProduct(productId);

        if(product.isBeforeOpenAt()) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_OPEN_TIME);
        }

        if(!product.getStatus().canOpen()) {
            throw new BusinessException(ErrorCode.INVALID_PRODUCT_STATE);
        }

        product.open();
        return new ProductStatusResponse(productId, product.getStatus());
    }

    @Override
    public ProductStatusResponse close(UUID productId) {
        Product product = findProduct(productId);
        product.close();
        return new ProductStatusResponse(productId, product.getStatus());
    }

    //메서드
    private Product findProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
