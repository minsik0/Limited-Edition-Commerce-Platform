package com.sparta.productservice.infrastructure.kafka;

import com.sparta.multi_module.common.Event.StockDecreaseEvent;
import com.sparta.multi_module.common.exception.BusinessException;
import com.sparta.multi_module.common.exception.ErrorCode;
import com.sparta.productservice.application.service.ProductOptionService;
import com.sparta.productservice.infrastructure.persistence.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.providers.base.Stock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockProcessService {

    private final ProductOptionRepository productOptionRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final ProductOptionService productOptionService;

    @Transactional
    public boolean process(StockDecreaseEvent event) {
        if(processedEventRepository.existsById(event.getEventId())) {
            return true;
        }

        try {
            productOptionService.decreaseStockAtomic(
                    event.getProductId(),
                    event.getOptionId(),
                    event.getQuantity()
            );

            processedEventRepository.save(new  ProcessedEvent(event.getEventId()));
            return true;

        } catch (BusinessException e) {
            if(e.getErrorCode() == ErrorCode.INSUFFICIENT_STOCK) {
                processedEventRepository.save(new  ProcessedEvent(event.getEventId()));
            }
            throw e;
        }
    }
}
