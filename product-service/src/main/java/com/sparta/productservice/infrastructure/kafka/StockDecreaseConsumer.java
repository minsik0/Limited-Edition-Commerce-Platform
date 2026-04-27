package com.sparta.productservice.infrastructure.kafka;

import com.sparta.multi_module.common.Event.StockDecreaseEvent;
import com.sparta.multi_module.common.Event.StockResultEvent;
import  com.sparta.productservice.application.service.ProductOptionService ;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@RequiredArgsConstructor
public class StockDecreaseConsumer {

    private final StockProcessService stockProcessService;
    private final StockResultProducer stockResultProducer;

    @KafkaListener(topics = "stock-decrease", groupId = "stock-group")
    public void consume(StockDecreaseEvent event) {

        boolean success = stockProcessService.process(event);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                stockResultProducer.send(
                        new StockResultEvent(
                                event.getEventId(),
                                event.getOrderId(),
                                success
                        )
                );
            }
        }
        );
    }
}
