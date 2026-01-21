package com.sparta.orderservice.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
    private UUID orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private UUID optionId;

    @Column(nullable = false)
    private String optionName;

    @Column(nullable = false)
    private int price; //주문 시점

    @Column(nullable = false)
    private int quantity;

    @Builder
    private OrderItem(
            UUID productId,
            String productName,
            UUID optionId,
            String optionName,
            int price,
            int quantity
    ) {
        this.productId = productId;
        this.productName = productName;
        this.optionId = optionId;
        this.optionName = optionName;
        this.price = price;
        this.quantity = quantity;
    }

    void assignOrder(Order order) {
        this.order = order;
    }

    public int calculateTotalPrice() {
        return price * quantity;
    }

}
