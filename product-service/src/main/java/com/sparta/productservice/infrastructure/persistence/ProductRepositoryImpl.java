package com.sparta.productservice.infrastructure.persistence;

import com.sparta.productservice.domain.product.Product;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Product> findFirstCursorPage(int size) {
        return em.createQuery("""
                select p
                from Product p
                where p.deletedAt is null
                order by p.openAt desc, p.productId desc
                """, Product.class)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Product> findNextCursorPage(UUID cursorId, LocalDateTime cursorOpenAt, int size) {
        return em.createQuery("""
                select p
                from Product p
                where p.deletedAt is null
                and (
                    p.openAt < :cursorOpenAt
                    or (p.openAt = :cursorOpenAt and p.productId < :cursorId)
                )
                order by p.openAt desc, p.productId desc
                """, Product.class)
                .setParameter("cursorId", cursorId)
                .setParameter("cursorOpenAt", cursorOpenAt)
                .setMaxResults(size)
                .getResultList();
    }
}