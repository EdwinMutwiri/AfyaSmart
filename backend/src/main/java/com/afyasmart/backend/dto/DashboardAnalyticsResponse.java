package com.afyasmart.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class DashboardAnalyticsResponse {

    // Summary Cards
    private long totalUsers;
    private long totalPatients;
    private long totalDoctors;
    private long totalAdmins;
    private long totalAppointments;

    private long pendingAppointments;
    private long confirmedAppointments;
    private long completedAppointments;
    private long cancelledAppointments;

    // Charts

    // Doctor Name -> Number of Appointments
    private Map<String, Long> doctorWorkload;

    // Appointment Status -> Count
    private Map<String, Long> appointmentStatus;

    // User Role -> Count
    private Map<String, Long> userRoles;
}