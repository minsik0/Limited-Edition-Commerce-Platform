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

        // 1. 멱등성 체크
        if (processedEventRepository.existsById(event.getEventId())) {
            return;
        }

        // 2. 재고 차감 시도 (성공:true / 실패:false)
        boolean success = productOptionService.decreaseStockAtomic(
                event.getProductId(),
                event.getOptionId(),
                event.getQuantity()
        );

        // 3. 처리 이벤트 저장 (중복 consume 방지)
        processedEventRepository.save(new ProcessedEvent(event.getEventId()));

        // 4. DB 커밋 후 결과 이벤트 발행
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
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