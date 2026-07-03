package com.sparta.orderservice.application.service;

import com.sparta.multi_module.common.Event.StockDecreaseEvent;
import com.sparta.multi_module.common.Event.StockIncreaseEvent;
import com.sparta.multi_module.common.exception.BusinessException;
import com.sparta.multi_module.common.exception.ErrorCode;
import com.sparta.orderservice.application.dto.request.CreateOrderRequest;
import com.sparta.orderservice.application.dto.response.CreateOrderResponse;
import com.sparta.orderservice.application.dto.response.ProductOptionForOrderResponse;
import com.sparta.orderservice.domain.order.OrderItem;
import com.sparta.orderservice.domain.order.Order;
import com.sparta.orderservice.domain.order.OrderStatus;
import com.sparta.orderservice.infrastructure.OrderRepository;
import com.sparta.orderservice.infrastructure.client.ProductClient;
import com.sparta.orderservice.infrastructure.kafka.StockEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public CreateOrderResponse create(UUID userId, CreateOrderRequest request) {
        //상품 조회
        ProductOptionForOrderResponse product = productClient
                .getProductOption(request.getProductId(),request.getOptionId());

        // 구매 한도 검증
        int alreadyOrdered = orderRepository.countByUserIdAndProductId(
                userId, request.getProductId()
        );
        if (alreadyOrdered + request.getQuantity() > product.getMaxPurchasePerUser()) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }

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
                .status(OrderStatus.PENDING)
                .build();

        order.addItem(item);

        Order savedOrder = orderRepository.save(order);

        StockDecreaseEvent event = StockDecreaseEvent.of(
                savedOrder.getOrderId(),
                request.getProductId(),
                request.getOptionId(),
                request.getQuantity()
        );

        eventPublisher.publishEvent(event);

        return new CreateOrderResponse(savedOrder.getOrderId(), savedOrder.getStatus());
    }

    @Override
    public void cancelOrder(UUID userId, UUID orderId) {
        Order order = orderRepository.findByOrderIdAndUserId(orderId, userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        order.cancel();

        if (order.getItems() != null) {
            order.getItems().stream()
                    .map(item -> StockIncreaseEvent.of(
                            order.getOrderId(),
                            item.getProductId(),
                            item.getOptionId(),
                            item.getQuantity()
                    ))
                    .forEach(eventPublisher::publishEvent);
        }
    }

    @Override
    public void markOrderAsPaid(UUID orderId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(()-> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        order.markAsPaid();
    }
}
