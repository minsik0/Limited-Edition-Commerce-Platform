package com.sparta.productservice.infrastructure.persistence;

import com.sparta.productservice.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByDeletedAtIsNull(Pageable pageable);

    @Query("""
        SELECT p
        FROM Product p
        WHERE p.deletedAt IS NULL 
        AND (
            :cursorOpenAt IS NULL 
            OR p.openAt < :cursorOpenAt
            OR (p.openAt = :cursorOpenAt AND p.productId < :cursorId)  
            )
            ORDER BY p.openAt DESC, p.productId DESC 
    """)
    List<Product> findCursorPage(
            @Param("cursorOpenAt")LocalDateTime cursorOpenAt,
            @Param("cursorId")UUID cursorId,
            Pageable pageable
            );
}
