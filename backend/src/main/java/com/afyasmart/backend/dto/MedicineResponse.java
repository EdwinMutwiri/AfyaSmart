package com.afyasmart.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class MedicineResponse {

    private Long id;

    private Long accountId;

    private String patientName;

    private String medicineName;

    private String dosage;

    private String frequency;

    private LocalDate startDate;

    private LocalDate endDate;

    /*
     * Multiple reminder times for this medicine.
     *
     * Example:
     * [07:30, 13:00, 19:00]
     */
    private List<LocalTime> reminderTimes;

    /*
     * Old single reminder field retained temporarily
     * for compatibility with existing frontend/data.
     */
    private LocalTime reminderTime;

    private String instructions;

    private Boolean active;

    private LocalDateTime createdAt;
}