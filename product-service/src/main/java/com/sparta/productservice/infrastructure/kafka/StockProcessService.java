package com.sparta.productservice.infrastructure.kafka;

import com.sparta.multi_module.common.Event.StockDecreaseEvent;
import com.sparta.multi_module.common.Event.StockResultEvent;
import com.sparta.multi_module.common.exception.BusinessException;
import com.sparta.multi_module.common.exception.ErrorCode;
import com.sparta.productservice.application.service.ProductOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class StockProcessService {

    private final ProductOptionService productOptionService;
    private final ProcessedEventRepository processedEventRepository;
    private final StockResultProducer stockResultProducer;

    @Transactional
    public void process(StockDecreaseEvent event) {

        if (processedEventRepository.existsById(event.getEventId())) {
            return;
        }

        boolean success;

        try {
            productOptionService.decreaseStockAtomic(
                    event.getProductId(),
                    event.getOptionId(),
                    event.getQuantity()
            );

            processedEventRepository.save(new ProcessedEvent(event.getEventId()));
            success = true;

        } catch (BusinessException e) {

            if (e.getErrorCode() == ErrorCode.INSUFFICIENT_STOCK) {
                processedEventRepository.save(new ProcessedEvent(event.getEventId()));
                success = false;
            } else {
                throw e;
            }
        }

        boolean finalSuccess = success;

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        stockResultProducer.send(
                                new StockResultEvent(
                                        event.getEventId(),
                                        event.getOrderId(),
                                        finalSuccess
                                )
                        );
                    }
                }
        );
    }
}
