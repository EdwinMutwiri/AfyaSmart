package com.afyasmart.backend.dto;

import com.afyasmart.backend.entity.MedicineDoseStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * ============================================================
 * AfyaSmart - Medicine Dose Response
 * ============================================================
 */

@Data
@Builder
public class MedicineDoseResponse {

    private Long id;

    private Long medicineId;

    private String medicineName;

    private String dosage;

    private LocalDate scheduledDate;

    private LocalTime scheduledTime;

    private MedicineDoseStatus status;

    private LocalDateTime takenAt;
}