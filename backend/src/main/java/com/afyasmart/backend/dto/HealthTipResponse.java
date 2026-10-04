package com.afyasmart.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HealthTipResponse {

    private Long id;

    private String title;

    private String content;

    private String category;

    private String conditionTag;

    private Boolean active;

    private LocalDateTime createdAt;
}