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

        // 1. 멱등성 체크 (이미 처리된 이벤트면 중복 재고차감 방지)
        if (processedEventRepository.existsById(event.getEventId())) {
            return;
        }

        boolean success;

        try {
            // 2. 재고 원자 차감
            productOptionService.decreaseStockAtomic(
                    event.getProductId(),
                    event.getOptionId(),
                    event.getQuantity()
            );

            // 3. 처리 완료 이벤트 기록
            processedEventRepository.save(new ProcessedEvent(event.getEventId()));
            success = true;

        } catch (BusinessException e) {

            // 재고 부족은 비즈니스 실패로 간주
            if (e.getErrorCode() == ErrorCode.INSUFFICIENT_STOCK) {
                processedEventRepository.save(new ProcessedEvent(event.getEventId()));
                success = false;
            } else {
                // 기타 예외는 Kafka 재시도를 위해 throw
                throw e;
            }
        }

        boolean finalSuccess = success;

        // 4. DB 커밋 성공 후에만 결과 이벤트 발행
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