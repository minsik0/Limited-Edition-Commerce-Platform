package com.sparta.productservice.infrastructure.kafka;

import com.sparta.multi_module.common.Event.StockIncreaseEvent;
import com.sparta.productservice.application.service.ProductOptionService;
import com.sparta.productservice.infrastructure.persistence.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockIncreaseProcessService {

    private final ProductOptionService productOptionService;
    private final ProcessedEventRepository processedEventRepository;
    private final ProductOptionRepository productOptionRepository;

    @Transactional
    public void process(StockIncreaseEvent event) {

        if(processedEventRepository.existsById(event.getProductId())) {
            return;
        }

        productOptionService.increaseStockAtomic(
                event.getProductId(),
                event.getOptionId(),
                event.getQuantity()
        );

        processedEventRepository.save(new ProcessedEvent(event.getEventId()));
    }
}
