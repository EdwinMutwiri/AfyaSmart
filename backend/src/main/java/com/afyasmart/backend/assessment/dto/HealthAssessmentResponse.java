package com.afyasmart.backend.assessment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HealthAssessmentResponse {

    private Double bmi;

    private Integer healthScore;

    private String riskLevel;

    private String recommendation;

    private LocalDateTime assessmentDate;

}