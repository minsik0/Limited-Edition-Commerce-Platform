package com.sparta.orderservice.infrastructure.kafka;

import com.sparta.multi_module.common.Event.StockResultEvent;
import com.sparta.multi_module.common.exception.BusinessException;
import com.sparta.multi_module.common.exception.ErrorCode;
import com.sparta.orderservice.domain.order.Order;
import com.sparta.orderservice.infrastructure.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderResultProcessService {

    private final OrderRepository orderRepository;

    public void process(StockResultEvent event) {
        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if(event.isSuccess()) {
            order.confirm();
        } else {
            order.fail();
        }
    }

}
