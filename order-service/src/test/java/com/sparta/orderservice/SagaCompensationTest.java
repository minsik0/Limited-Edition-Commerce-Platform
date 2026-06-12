package com.sparta.orderservice;

import com.sparta.multi_module.common.Event.StockResultEvent;
import com.sparta.orderservice.domain.order.Order;
import com.sparta.orderservice.domain.order.OrderStatus;
import com.sparta.orderservice.infrastructure.OrderRepository;
import com.sparta.orderservice.infrastructure.kafka.OrderResultProcessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class SagaCompensationTest {

    @Autowired
    OrderResultProcessService orderResultProcessService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    void 재고_부족시_주문_FAILED_처리() {
        // given: PENDING 상태 주문
        Order order = Order.builder()
                .userId(UUID.randomUUID())
                .status(OrderStatus.PENDING)
                .build();
        orderRepository.save(order);

        StockResultEvent failEvent = new StockResultEvent(
                UUID.randomUUID(), order.getOrderId(), false
        );

        // when
        orderResultProcessService.process(failEvent);

        // then
        Order result = orderRepository.findById(order.getOrderId())
                .orElseThrow(() -> new AssertionError("주문을 찾을 수 없습니다."));
        assertThat(result.getStatus()).isEqualTo(OrderStatus.FAILED);
    }
}

