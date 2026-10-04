package com.afyasmart.backend.telemedicine.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class TelemedicineResponse {

    private Long id;

    private Long appointmentId;

    private String patientName;

    private String patientEmail;

    private String doctorName;

    private String specialization;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private String reason;

    private String meetingLink;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;
}