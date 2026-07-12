package com.afyasmart.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DoctorProfileRequest {

    private Long accountId;

    private Long specializationId;

    private String hospital;

    private String licenseNumber;

    private Integer yearsExperience;

    private BigDecimal consultationFee;

    private String bio;

    private Boolean available;

}