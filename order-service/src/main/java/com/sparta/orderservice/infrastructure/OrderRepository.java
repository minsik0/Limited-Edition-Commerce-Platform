package com.sparta.orderservice.infrastructure;

import com.sparta.orderservice.domain.order.Order;
import com.sparta.orderservice.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByUserId(UUID userId);

    List<Order> findAllByUserIdAndStatus(UUID userId, OrderStatus status);

    Optional<Order> findByOrderIdAndUserId(UUID orderId, UUID userId);
}
