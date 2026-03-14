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
    select p
    from Product p
    where p.deletedAt is null 
    order by p.openAt desc, p.productId desc 
    """)
    List<Product> findFirstCursorPage(Pageable pageable);

    @Query("""
    select p
    from Product p
    where p.deletedAt is null 
    and(
        p.openAt < :cursorOpenAt
        or (p.openAt = :cursorOpenAt and p.productId < cursorId)
        )
    order by p.openAt desc, p.productId desc 
    """)
    List<Product> findNextCursorPage(
            @Param("cursorId") UUID cursorId,
            @Param("cursorOpenAt") LocalDateTime cursorOpenAt,
            Pageable pageable);

}
