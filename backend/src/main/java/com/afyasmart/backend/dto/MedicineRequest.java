package com.afyasmart.backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * ============================================================
 * AfyaSmart - Medicine Request DTO
 * ============================================================
 *
 * Carries medicine information from the frontend to the
 * backend when creating or updating a medicine reminder.
 * ============================================================
 */
@Data
public class MedicineRequest {

    /**
     * Patient account ID.
     */
    private Long accountId;

    /**
     * Name of the medicine.
     */
    private String medicineName;

    /**
     * Dosage information.
     *
     * Example:
     * 500 mg
     * 2 tablets
     */
    private String dosage;

    /**
     * How frequently the medicine should be taken.
     */
    private String frequency;

    /**
     * Date the medicine schedule starts.
     */
    private LocalDate startDate;

    /**
     * Date the medicine schedule ends.
     */
    private LocalDate endDate;

    /**
     * Time of the medicine reminder.
     */
    private LocalTime reminderTime;

    /**
     * Additional instructions.
     *
     * Example:
     * Take after meals.
     */
    private String instructions;
}