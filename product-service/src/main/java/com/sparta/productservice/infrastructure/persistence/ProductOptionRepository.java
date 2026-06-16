package com.sparta.productservice.infrastructure.persistence;

import com.sparta.productservice.domain.option.ProductOption;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Transactional
public interface ProductOptionRepository extends JpaRepository<ProductOption, UUID> {
    List<ProductOption> findByProductProductIdAndDeletedAtIsNull(UUID productId);

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

    Optional<ProductOption> findByOptionIdAndProduct_ProductId(UUID optionId, UUID productId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT o FROM ProductOption o
    WHERE o.optionId = :optionId
      AND o.product.productId = :productId
    """)
    Optional<ProductOption> findForUpdate(@Param("optionId") UUID optionId, @Param("productId") UUID productId);

    @Modifying
    @Query("""
    UPDATE ProductOption o
    SET o.remainStock = o.remainStock - :quantity
    WHERE o.optionId = :optionId
      AND o.product.productId = :productId
      AND o.remainStock >= :quantity
    """)
    int decreaseStockAtomic(
            @Param("optionId") UUID optionId,
            @Param("productId") UUID productId,
            @Param("quantity") int quantity
    );

}
