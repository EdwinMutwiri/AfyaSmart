package com.afyasmart.backend.service;

import com.afyasmart.backend.dto.MedicineDoseResponse;
import com.afyasmart.backend.dto.MedicineRequest;
import com.afyasmart.backend.dto.MedicineResponse;

import java.util.List;

public interface MedicineService {

    // =========================================================
    // MEDICINE CRUD
    // =========================================================

    MedicineResponse createMedicine(MedicineRequest request);

    MedicineResponse getMedicine(Long medicineId);

    List<MedicineResponse> getPatientMedicines(Long accountId);

    List<MedicineResponse> getActivePatientMedicines(Long accountId);

    MedicineResponse updateMedicine(
            Long medicineId,
            MedicineRequest request
    );

    MedicineResponse deactivateMedicine(Long medicineId);

    void deleteMedicine(Long medicineId);


    // =========================================================
    // MEDICINE DOSE TRACKING
    // =========================================================

    /*
     * Mark one specific scheduled dose as taken.
     *
     * We now use doseId instead of medicineId because
     * one medicine can have several doses in one day.
     */
    MedicineDoseResponse markDoseAsTaken(Long doseId);


    /*
     * Get all scheduled doses for today.
     *
     * Example:
     *
     * Paracetamol 07:30 - PENDING
     * Paracetamol 13:00 - TAKEN
     * Paracetamol 19:00 - PENDING
     */
    List<MedicineDoseResponse> getTodayDoses(Long accountId);


    /*
     * Get the complete dose history for a medicine.
     */
    List<MedicineDoseResponse> getMedicineDoseHistory(
            Long medicineId
    );
}