package com.sparta.productservice.infrastructure.persistence;

import com.sparta.productservice.domain.option.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductOptionRepository extends JpaRepository<ProductOption, UUID> {
    List<ProductOption> findByProductIdAndDeletedAtIsNull(UUID productId);

    @Query("""
        SELECT COALESCE(SUM(o.remainStock), 0)
        FROM ProductOption o
        WHERE o.product.productId = :productId
          AND o.deletedAt IS NULL
    """)
    int sumRemainStockByProductId(@Param("productId") UUID productId);

    @Query("""
        SELECT COUNT(o)
        FROM ProductOption o
        WHERE o.product.productId = :productId
          AND o.remainStock > 0
          AND o.deletedAt IS NULL
    """)
    long countRemainStockByProductId(@Param("productId") UUID productId);
}
