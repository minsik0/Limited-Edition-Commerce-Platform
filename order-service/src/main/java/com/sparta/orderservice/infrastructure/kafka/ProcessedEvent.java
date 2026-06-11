package com.sparta.orderservice.infrastructure.kafka;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "processed_event")
@Getter
@NoArgsConstructor
public class ProcessedEvent {

    @Id
    private UUID eventId;

    private LocalDateTime createdAt;

    public ProcessedEvent(UUID eventId) {
        this.eventId = eventId;
        this.createdAt = LocalDateTime.now();
    }
}
