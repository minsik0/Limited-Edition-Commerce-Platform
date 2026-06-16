package com.sparta.productservice.infrastructure.kafka;

import com.sparta.multi_module.common.Event.StockDecreaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockDecreaseConsumer {

    private final StockProcessService stockProcessService;

    @KafkaListener(topics = "stock-decrease", groupId = "stock-group")
    public void consume(StockDecreaseEvent event) {
        stockProcessService.process(event);
    }
}
