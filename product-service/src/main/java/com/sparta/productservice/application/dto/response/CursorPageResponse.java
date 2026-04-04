package com.sparta.productservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CursorPageResponse {

    private List<ProductSummaryResponse> contents;
    private boolean hasNext;
    private UUID nextCursorId;
    private LocalDateTime nextCursorOpenAt;
}
