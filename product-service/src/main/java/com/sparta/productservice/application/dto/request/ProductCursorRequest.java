package com.sparta.productservice.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCursorRequest {

    private UUID cursorId;
    private int size = 20;
    private LocalDateTime cursorOpenAt;
}
