package com.afyasmart.backend.service;

import com.afyasmart.backend.dto.MedicineRequest;
import com.afyasmart.backend.dto.MedicineResponse;

import java.util.List;

/**
 * ============================================================
 * AfyaSmart - Medicine Service
 * ============================================================
 *
 * Defines the business operations for managing medicine
 * reminders.
 *
 * ============================================================
 */
public interface MedicineService {

    /**
     * Create a new medicine reminder.
     *
     * @param request medicine information
     * @return created medicine
     */
    MedicineResponse createMedicine(MedicineRequest request);


    /**
     * Get a medicine by its ID.
     *
     * @param medicineId medicine ID
     * @return medicine information
     */
    MedicineResponse getMedicine(Long medicineId);


    /**
     * Get all medicines belonging to a patient.
     *
     * @param accountId patient account ID
     * @return list of medicines
     */
    List<MedicineResponse> getPatientMedicines(Long accountId);


    /**
     * Get only active medicines belonging to a patient.
     *
     * @param accountId patient account ID
     * @return active medicine reminders
     */
    List<MedicineResponse> getActivePatientMedicines(Long accountId);


    /**
     * Update an existing medicine reminder.
     *
     * @param medicineId medicine ID
     * @param request updated medicine information
     * @return updated medicine
     */
    MedicineResponse updateMedicine(
            Long medicineId,
            MedicineRequest request
    );


    /**
     * Deactivate a medicine reminder.
     *
     * The record is not deleted from the database.
     *
     * @param medicineId medicine ID
     * @return updated medicine
     */
    MedicineResponse deactivateMedicine(Long medicineId);


    /**
     * Delete a medicine record permanently.
     *
     * @param medicineId medicine ID
     */
    void deleteMedicine(Long medicineId);
}