package com.afyasmart.backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class MedicineRequest {

    /*
     * Patient account ID.
     */
    private Long accountId;

    /*
     * Name of the medicine.
     *
     * Example:
     * Paracetamol
     */
    private String medicineName;

    /*
     * Dosage.
     *
     * Example:
     * 500 mg
     */
    private String dosage;

    /*
     * Frequency.
     *
     * Examples:
     * Once daily
     * Twice daily
     * 3 times daily
     * 4 times daily
     * As needed
     */
    private String frequency;

    /*
     * Date medication starts.
     */
    private LocalDate startDate;

    /*
     * Optional medication end date.
     */
    private LocalDate endDate;

    /*
     * ---------------------------------------------------------
     * MULTIPLE REMINDER TIMES
     * ---------------------------------------------------------
     *
     * Example for 3 times daily:
     *
     * [
     *     "07:30",
     *     "13:00",
     *     "19:00"
     * ]
     *
     * Example for 4 times daily:
     *
     * [
     *     "07:30",
     *     "12:00",
     *     "18:00",
     *     "00:00"
     * ]
     */
    private List<LocalTime> reminderTimes;

    /*
     * Instructions for taking the medicine.
     *
     * Example:
     * Take after meals.
     */
    private String instructions;
}