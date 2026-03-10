package com.sparta.productservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CursorPageResponse <T>{

    private List<T> contents;
    private UUID nextCursorId;
    private LocalDateTime nextCursorOpenAt;
}
