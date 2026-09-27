package com.afyasmart.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * ============================================================
 * AfyaSmart - Medicine Response DTO
 * ============================================================
 *
 * Represents medicine information returned by the backend
 * to the frontend.
 * ============================================================
 */
@Data
@Builder
public class MedicineResponse {

    /**
     * Medicine record ID.
     */
    private Long id;

    /**
     * Patient account ID.
     */
    private Long accountId;

    /**
     * Patient's name.
     */
    private String patientName;

    /**
     * Name of the medicine.
     */
    private String medicineName;

    /**
     * Dosage information.
     */
    private String dosage;

    /**
     * Frequency of taking the medicine.
     */
    private String frequency;

    /**
     * Medicine schedule start date.
     */
    private LocalDate startDate;

    /**
     * Medicine schedule end date.
     */
    private LocalDate endDate;

    /**
     * Reminder time.
     */
    private LocalTime reminderTime;

    /**
     * Additional instructions.
     */
    private String instructions;

    /**
     * Whether the reminder is active.
     */
    private Boolean active;

    /**
     * Date and time the medicine record was created.
     */
    private LocalDateTime createdAt;
}