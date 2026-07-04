package com.sparta.productservice.infrastructure.kafka;

import com.sparta.multi_module.common.Event.StockIncreaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockIncreaseConsumer {

    private final StockIncreaseProcessService stockIncreaseProcessService;

    @KafkaListener(topics = "stock-increase", groupId = "stock-increase-group")
    public void consume(StockIncreaseEvent event) {
        stockIncreaseProcessService.process(event);
    }
}
