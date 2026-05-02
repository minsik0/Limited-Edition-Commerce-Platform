package com.sparta.productservice.application.service;

import com.sparta.multi_module.common.exception.BusinessException;
import com.sparta.multi_module.common.exception.ErrorCode;
import com.sparta.productservice.application.dto.request.ProductOptionCreateRequest;
import com.sparta.productservice.application.dto.request.ProductOptionUpdateRequest;
import com.sparta.productservice.application.dto.response.ProductOptionForOrderResponse;
import com.sparta.productservice.application.dto.response.ProductOptionResponse;
import com.sparta.productservice.domain.option.ProductOption;
import com.sparta.productservice.domain.product.Product;
import com.sparta.productservice.infrastructure.persistence.ProductOptionRepository;
import com.sparta.productservice.infrastructure.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductOptionServiceImpl implements ProductOptionService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    @Override
    public UUID create(UUID productId, ProductOptionCreateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductOption option = ProductOption.builder()
                .product(product)
                .size(request.getSize())
                .color(request.getColor())
                .remainStock(request.getStock())
                .build();

        productOptionRepository.save(option);
        return option.getOptionId();
    }

    @Override
    public void update(UUID optionId, ProductOptionUpdateRequest request) {
        ProductOption option = findOption(optionId);

        option.update(request.getSize(), request.getColor(), request.getStock());
    }

    @Override
    public void delete(UUID optionId) {
        ProductOption option = findOption(optionId);
        option.delete();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductOptionResponse> getByProductId(UUID productId) {
        return productOptionRepository.findByProductProductIdAndDeletedAtIsNull(productId)
                .stream()
                .map(ProductOptionResponse::from)
                .toList();
    }

    @Override
    public ProductOptionForOrderResponse getOptionForOrder(UUID productId, UUID optionId) {

        ProductOption option = productOptionRepository
                .findByOptionIdAndProduct_ProductId(optionId, productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OPTION_NOT_FOUND));

        return ProductOptionForOrderResponse.from(option.getProduct(), option);
    }

    @Override
    public void deductStock(UUID productId, UUID optionId, int quantity) {

        ProductOption option = productOptionRepository.findForUpdate(optionId, productId).
                orElseThrow(() -> new BusinessException(ErrorCode.OPTION_NOT_FOUND));

        option.decreaseStock(quantity);
    }

    @Override
    @Transactional
    public boolean decreaseStockAtomic(UUID productId, UUID optionId, int quantity) {

        int updated = productOptionRepository.decreaseStockAtomic(optionId, productId, quantity);

        return updated > 0;
    }

    private ProductOption findOption(UUID optionId) {
        return productOptionRepository.findById(optionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OPTION_NOT_FOUND));
    }
}
