package com.sparta.productservice.application.service;

import com.sparta.multi_module.common.exception.BusinessException;
import com.sparta.multi_module.common.exception.ErrorCode;
import com.sparta.productservice.application.dto.request.ProductCursorRequest;
import com.sparta.productservice.application.dto.response.CursorPageResponse;
import com.sparta.productservice.application.dto.response.ProductResponse;
import com.sparta.productservice.application.dto.response.ProductSummaryResponse;
import com.sparta.productservice.domain.product.Product;
import com.sparta.productservice.infrastructure.persistence.ProductOptionRepository;
import com.sparta.productservice.infrastructure.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    @Override
    public Page<ProductSummaryResponse> getPage(Pageable pageable) {

        Pageable sortPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "openAt")
        );
        return productRepository.findByDeletedAtIsNull(sortPageable)
                .map(ProductSummaryResponse::from);
    }

    @Override
    public ProductResponse get(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        int currentQuantity = productOptionRepository.sumRemainStockByProductId(productId);

        return ProductResponse.of(product, currentQuantity);
    }

    @Override
    @Cacheable(value = "products", key = "#request.cursorOpenAt + '_' + #request.cursorId + '_' + #request.size")
    public CursorPageResponse<ProductSummaryResponse> getCursorPage(ProductCursorRequest request) {

        int size = request.getSize();

        List<Product> products;

        if (request.getCursorOpenAt() == null || request.getCursorId() == null) {

            products = productRepository.findFirstCursorPage(size + 1);

        } else {

            products = productRepository.findNextCursorPage(
                    request.getCursorId(),
                    request.getCursorOpenAt(),
                    size + 1
            );
        }

        boolean hasNext = products.size() > size;

        if(hasNext) {
            products.remove(size);
        }

        List<ProductSummaryResponse> contents = products
                .stream()
                .map(ProductSummaryResponse::from)
                .collect(Collectors.toList());

        LocalDateTime nextCursorOpenAt = null;
        UUID nextCursorId = null;

        if (!products.isEmpty()) {
            Product last = products.get(products.size() - 1);
            nextCursorOpenAt = last.getOpenAt();
            nextCursorId = last.getProductId();
        }

        return new CursorPageResponse<>(contents, hasNext, nextCursorId, nextCursorOpenAt);
    }

}
