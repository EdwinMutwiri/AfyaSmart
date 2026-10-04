package com.afyasmart.backend.dto;

import lombok.Data;

@Data
public class HealthTipRequest {

    private String title;

    private String content;

    private String category;

    private String conditionTag;

    private Boolean active;
}