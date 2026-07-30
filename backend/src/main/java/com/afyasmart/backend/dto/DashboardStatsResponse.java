package com.afyasmart.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsResponse {

    private long totalUsers;

    private long totalPatients;

    private long totalDoctors;

    private long totalAppointments;

    private long pendingAppointments;

    private long confirmedAppointments;

    private long completedAppointments;

    private long cancelledAppointments;

}