package com.sparta.orderservice.infrastructure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaConsumerConfig {

    /**
     * Kafka Consumer 에러 핸들러
     *
     * - 메시지 처리 실패 시 1초 간격으로 최대 3회 재시도
     * - 3회 모두 실패하면 DLQ(Dead Letter Queue)로 전송
     * - DLQ 토픽명: {원본 토픽}.DLT (예: stock-result.DLT)
     */
    @Bean
    public CommonErrorHandler commonErrorHandler(KafkaTemplate<String, Object> kafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (ConsumerRecord<?, ?> record, Exception ex) -> {
                    log.error("[DLQ] 메시지 처리 실패 → DLQ 전송 | topic={}, key={}",
                            record.topic(), record.key(), ex);
                    return new org.apache.kafka.common.TopicPartition(
                            record.topic() + ".DLT", -1);
                });

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                recoverer,
                new FixedBackOff(1000L, 3)  // 1초 간격, 최대 3회 재시도
        );

        errorHandler.addNotRetryableExceptions(
                com.sparta.multi_module.common.exception.BusinessException.class
        );

        return errorHandler;
    }
}
