package com.sparta.orderservice.domain.order;

import com.sparta.multi_module.common.exception.BusinessException;
import com.sparta.multi_module.common.exception.ErrorCode;
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
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false, unique = true)
    private UUID orderId;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private int totalPrice;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Builder
    private Order(UUID userId, OrderStatus status) {
        this.userId = userId;
        this.status = (status != null) ? status : OrderStatus.CREATED;
        this.totalPrice = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
        this.totalPrice += item.calculateTotalPrice();
        item.assignOrder(this);
    }

    public void cancel() {
        if (!status.isCancelable()) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_CANCEL);
        }
        this.status = OrderStatus.CANCELED;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsPaid() {
        if (this.status == OrderStatus.PAID) {
            return;
        }

        if (this.status != OrderStatus.CREATED) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS);
        }

        this.status = OrderStatus.PAID;
        this.updatedAt = LocalDateTime.now();
    }

    public void confirm() {
        if (this.status == OrderStatus.CREATED) {
            return;
        }
        if (this.status != OrderStatus.PENDING) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS);
        }
        this.status = OrderStatus.CREATED;
        this.updatedAt = LocalDateTime.now();
    }

    public void fail() {
        if (this.status == OrderStatus.FAILED) {
            return;
        }
        if (this.status != OrderStatus.PENDING) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS);
        }
        this.status = OrderStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }
}
