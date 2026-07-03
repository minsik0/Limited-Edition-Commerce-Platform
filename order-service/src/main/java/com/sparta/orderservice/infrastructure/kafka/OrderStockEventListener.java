package com.sparta.orderservice.infrastructure.kafka;

import com.sparta.multi_module.common.Event.StockDecreaseEvent;
import com.sparta.multi_module.common.Event.StockIncreaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderStockEventListener {

    private final StockEventProducer stockEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(StockDecreaseEvent event) {
        stockEventProducer.send(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(StockIncreaseEvent event) {
        stockEventProducer.send(event);
    }
}
