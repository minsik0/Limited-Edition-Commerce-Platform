package com.sparta.orderservice.infrastructure;

import com.sparta.orderservice.infrastructure.kafka.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, UUID> {
}
