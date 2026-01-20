package com.sparta.productservice.domain.product;

import com.sparta.productservice.domain.option.ProductOption;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private UUID productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int maxPurchasePerUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(nullable = false)
    private LocalDateTime openAt;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ProductOption> options = new ArrayList<>();

    @Builder
    protected Product(
            String name,
            int price,
            int maxPurchasePerUser,
            LocalDateTime openAt,
            ProductStatus status
    ) {
        this.name = name;
        this.price = price;
        this.maxPurchasePerUser = maxPurchasePerUser;
        this.openAt = openAt;
        this.status = status == null ? ProductStatus.READY : status;
    }

    /* ================= 도메인 메서드 ================= */

    public void updateInfo(String name, int price, int maxPurchasePerUser, LocalDateTime openAt) {
        this.name = name;
        this.price = price;
        this.maxPurchasePerUser = maxPurchasePerUser;
        this.openAt = openAt;
    }

    public void open() {
        this.status = ProductStatus.OPEN;
    }

    public void close() {
        this.status = ProductStatus.CLOSED;
        this.deletedAt = LocalDateTime.now();
        this.options.forEach(ProductOption::delete);
    }

    public void soldOut() {
        this.status = ProductStatus.SOLD_OUT;
    }

    public boolean isBeforeOpenAt() {
        return LocalDateTime.now().isBefore(this.openAt);
    }
}
