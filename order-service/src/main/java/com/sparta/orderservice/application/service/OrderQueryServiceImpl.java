package com.sparta.orderservice.application.service;

import com.sparta.orderservice.application.dto.response.OrderItemResponse;
import com.sparta.orderservice.application.dto.response.OrderResponse;
import com.sparta.orderservice.application.dto.response.OrderSummaryResponse;
import com.sparta.orderservice.domain.order.Order;
import com.sparta.orderservice.domain.order.OrderItem;
import com.sparta.orderservice.domain.order.OrderStatus;
import com.sparta.orderservice.global.exception.BusinessException;
import com.sparta.orderservice.global.exception.ErrorCode;
import com.sparta.orderservice.infrastructure.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderSummaryResponse> getOrders(UUID userId, OrderStatus status) {
        List<Order> orders = (status == null)
                ? orderRepository.findAllByUserId(userId)
                : orderRepository.findAllByUserIdAndStatus(userId, status);

        return orders.stream()
                .map(order ->  new OrderSummaryResponse(
                        order.getOrderId(),
                        order.getStatus(),
                        order.getTotalPrice(),
                        order.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public OrderResponse getOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findByOrderIdAndUserId(orderId, userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        return  new OrderResponse(
                order.getOrderId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getItems()
                        .stream()
                        .map(this::toItemResponse)
                        .toList(),
                order.getCreatedAt()
        );

    }

    /*
    * @param item 변환할 OrderItem 엔티티
    * @return 변환된 OrderItemResponse DTO
    * */
    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getOptionId(),
                item.getOptionName(),
                item.getPrice(),
                item.getQuantity()
        );
    }

}
