package com.afyasmart.backend.assessment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HealthAssessmentResponse {

    private Double bmi;

    private Integer healthScore;

    private String riskLevel;

    private String recommendation;

}