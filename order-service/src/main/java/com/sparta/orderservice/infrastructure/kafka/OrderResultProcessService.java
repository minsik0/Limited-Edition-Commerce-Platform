package com.sparta.orderservice.infrastructure.kafka;

import com.sparta.multi_module.common.Event.StockResultEvent;
import com.sparta.multi_module.common.exception.BusinessException;
import com.sparta.multi_module.common.exception.ErrorCode;
import com.sparta.orderservice.domain.order.Order;
import com.sparta.orderservice.infrastructure.OrderRepository;
import com.sparta.orderservice.infrastructure.ProcessedEventRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderResultProcessService {

    private final OrderRepository orderRepository;
    private final ProcessedEventRepository processedEventRepository;  // ★ 주입

    public void process(StockResultEvent event) {

        // ★ 멱등성 체크
        if (processedEventRepository.existsById(event.getEventId())) {
            return;
        }

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if (event.isSuccess()) {
            order.confirm();
        } else {
            order.fail();
        }

        // 처리 이벤트 저장
        processedEventRepository.save(new ProcessedEvent(event.getEventId()));
    }
}
