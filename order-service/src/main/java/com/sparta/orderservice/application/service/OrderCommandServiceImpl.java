package com.sparta.orderservice.application.service;

import com.sparta.orderservice.application.dto.request.CreateOrderRequest;
import com.sparta.orderservice.application.dto.response.CreateOrderResponse;
import com.sparta.orderservice.domain.order.OrderItem;
import com.sparta.orderservice.domain.order.Order;
import com.sparta.orderservice.domain.order.OrderStatus;
import com.sparta.orderservice.global.exception.BusinessException;
import com.sparta.orderservice.global.exception.ErrorCode;
import com.sparta.orderservice.infrastructure.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;

    @Override
    public CreateOrderResponse create(UUID userId, CreateOrderRequest request) {

        /*
         * TODO
         * 1. Product 서비스 호출
         * - 상품/옵션 조회
         * - 가격, 이름 스냅샷 확보
         * 2. 재고 차감 (추후)
         */

        //임시 데이터
        String productName = "임시 상품명";
        String optionName = "임시 옵션명";
        int price = 100_000;

        OrderItem item = OrderItem.builder()
                .productId(request.getProductId())
                .productName(productName)
                .optionId(request.getOptionId())
                .optionName(optionName)
                .price(price)
                .quantity(request.getQuantity())
                .build();

        int totalPrice = item.calculateTotalPrice();

        Order order = Order.builder()
                .userId(userId)
                .status(OrderStatus.CREATED)
                .totalPrice(totalPrice)
                .build();

        order.addItem(item);

        Order savedOrder = orderRepository.save(order);

        return new CreateOrderResponse(savedOrder.getOrderId(), savedOrder.getStatus());
    }

    @Override
    public void cancelOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        order.cancel();
    }
}
