package com.sparta.productservice.infrastructure.kafka;

import com.sparta.multi_module.common.Event.StockResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockResultProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "stock-result";

    public void send(StockResultEvent event) {
        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);
    }
}
