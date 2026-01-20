package com.sparta.productservice.domain.option;

import com.sparta.productservice.domain.product.Product;
import com.sparta.productservice.global.exception.BusinessException;
import com.sparta.productservice.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_options")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption {

    @Id
    @GeneratedValue
    @Column(name = "option_id")
    private UUID optionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private int remainStock;

    private LocalDateTime deletedAt;

    // 🔹 Builder
    @Builder
    protected ProductOption(
            Product product,
            String size,
            String color,
            int remainStock
    ) {
        this.product = product;
        this.size = size;
        this.color = color;
        this.remainStock = remainStock;
    }

    /* ================= 도메인 메서드 ================= */

    public void update(String size, String color, Integer stock) {
        if (size != null) this.size = size;
        if (color != null) this.color = color;
        if (stock != null) this.remainStock = stock;
    }

    public void decreaseStock(int quantity) {
        if (this.remainStock < quantity) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        }
        this.remainStock -= quantity;
    }

    public boolean isOutOfStock() {
        return this.remainStock == 0;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
