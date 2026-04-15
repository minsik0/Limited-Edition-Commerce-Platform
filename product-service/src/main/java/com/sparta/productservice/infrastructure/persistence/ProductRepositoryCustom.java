package com.sparta.productservice.infrastructure.persistence;

import com.sparta.productservice.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ProductRepositoryCustom {

    List<Product> findFirstCursorPage(int size);
    List<Product> findNextCursorPage(UUID corsorId, LocalDateTime cursorOpenAt, int size);
}
