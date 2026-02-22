package com.sparta.orderservice.application.service;

import com.sparta.multi_module.common.exception.BusinessException;
import com.sparta.multi_module.common.exception.ErrorCode;
import com.sparta.orderservice.application.dto.request.CreateOrderRequest;
import com.sparta.orderservice.application.dto.response.CreateOrderResponse;
import com.sparta.orderservice.application.dto.response.ProductOptionForOrderResponse;
import com.sparta.orderservice.domain.order.OrderItem;
import com.sparta.orderservice.domain.order.Order;
import com.sparta.orderservice.infrastructure.OrderRepository;
import com.sparta.orderservice.infrastructure.client.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @Override
    public CreateOrderResponse create(UUID userId, CreateOrderRequest request) {
        //상품 조회
        ProductOptionForOrderResponse product = productClient
                .getProductOption(request.getProductId(),request.getOptionId());
        //재고 검증
        if(product.getRemainStock() < request.getQuantity()) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        }
        //재고 차감
        productClient.deductStock(
                request.getProductId(),
                request.getOptionId(),
                request.getQuantity()
        );
        //Item 생성
        OrderItem item = OrderItem.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .optionId(product.getOptionId())
                .optionName(product.getOptionName())
                .price(product.getPrice())
                .quantity(request.getQuantity())
                .build();

        Order order = Order.builder()
                .userId(userId)
                .build();

        order.addItem(item);

        Order savedOrder = orderRepository.save(order);

        return new CreateOrderResponse(savedOrder.getOrderId(), savedOrder.getStatus());
    }

    @Override
    public void cancelOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findByOrderIdAndUserId(orderId, userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        order.cancel();
    }

    @Override
    public void markOrderAsPaid(UUID orderId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(()-> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        order.markAsPaid();
    }
}
