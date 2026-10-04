package com.afyasmart.backend.dto;

import lombok.Data;

/**
 * ============================================================
 * AfyaSmart - Health Tip Recommendation Request
 * ============================================================
 *
 * Contains the health information used by the recommendation
 * engine to determine which health tip is most relevant.
 *
 * The information can come from the patient's latest health
 * assessment and other available AfyaSmart information.
 * ============================================================
 */
@Data
public class HealthTipRecommendationRequest {

    /**
     * Patient's BMI.
     *
     * Example:
     * 24.5
     */
    private Double bmi;

    /**
     * Overall risk level from the health assessment.
     *
     * Examples:
     * LOW
     * MEDIUM
     * HIGH
     */
    private String riskLevel;

    /**
     * Optional preferred health category.
     *
     * Examples:
     * NUTRITION
     * EXERCISE
     * SLEEP
     * HYDRATION
     * MEDICATION
     */
    private String category;

    /**
     * Optional condition or health tag.
     *
     * Examples:
     * GENERAL
     * OVERWEIGHT
     * UNDERWEIGHT
     * HIGH_RISK
     */
    private String conditionTag;
}