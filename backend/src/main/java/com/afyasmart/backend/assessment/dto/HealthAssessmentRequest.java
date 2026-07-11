package com.afyasmart.backend.assessment.dto;

import lombok.Data;

@Data
public class HealthAssessmentRequest {

    private Long accountId;

    private Integer age;

    private String gender;

    private Double height;

    private Double weight;

    private Integer systolic;

    private Integer diastolic;

    private Double bloodSugar;

    private Integer exerciseDays;

    private Boolean smoker;

    private Boolean alcohol;

}