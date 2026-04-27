package com.sparta.orderservice.infrastructure.kafka.producer;

import com.sparta.multi_module.common.Event.StockResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockResultConsumer {

    private final OrderResultProcessService orderResultProcessService;

    @KafkaListener(topics = "stock-result", groupId = "order-group")
    public void consume(StockResultEvent event) {
        orderResultProcessService.process(event);
    }
}
