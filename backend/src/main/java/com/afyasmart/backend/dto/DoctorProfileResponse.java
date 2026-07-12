package com.afyasmart.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DoctorProfileResponse {

    private Long id;

    private Long accountId;

    private String doctorName;

    private String specialization;

    private String hospital;

    private String licenseNumber;

    private Integer yearsExperience;

    private BigDecimal consultationFee;

    private String bio;

    private Boolean available;

}