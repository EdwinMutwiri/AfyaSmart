package com.afyasmart.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardResponse {

    private Long totalPatients;

    private Long totalDoctors;

    private Long totalAppointments;

    private Long totalAssessments;

}