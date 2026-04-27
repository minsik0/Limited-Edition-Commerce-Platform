package com.sparta.productservice.infrastructure.kafka;

import com.sparta.multi_module.common.Event.StockDecreaseEvent;
import com.sparta.multi_module.common.Event.StockResultEvent;
import com.sparta.productservice.application.service.ProductOptionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockDecreaseConsumer {

    private final ProductOptionServiceImpl productOptionService;
    private final ProcessedEventRepository processedEventRepository;
    private final StockResultProducer stockResultProducer;

    @KafkaListener(topics = "stock-decrese", groupId = "stock-group")
    public void stockDecreaseConsumer(StockDecreaseEvent event) {

        if (processedEventRepository.existsById(event.getEventId())) {
            return;
        }

        boolean success = true;

        try {
            productOptionService.decreaseStockAtomic(
                    event.getProductId(),
                    event.getOptionId(),
                    event.getQuantity()
            );
        } catch (Exception e) {
            success = false;
        }

        stockResultProducer.send(
                new StockResultEvent(event.getEventId(), event.getOrderId(), success)
        );

        processedEventRepository.save(new ProcessedEvent(event.getEventId()));
    }
}
