package com.sparta.orderservice.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import com.sparta.multi_module.common.Event.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "stock-decrease";
    private static final String INCREASE_TOPIC = "stock-increase";

    public void send(StockDecreaseEvent event) {
        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);
    }

    public void send(StockIncreaseEvent event) {
        kafkaTemplate.send(INCREASE_TOPIC, event.getOrderId().toString(), event);
    }
}
