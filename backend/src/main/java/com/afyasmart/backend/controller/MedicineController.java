package com.afyasmart.backend.controller;

import com.afyasmart.backend.dto.MedicineRequest;
import com.afyasmart.backend.dto.MedicineResponse;
import com.afyasmart.backend.service.MedicineService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ============================================================
 * AfyaSmart - Medicine Controller
 * ============================================================
 *
 * REST API endpoints for managing patient medicine reminders.
 *
 * Base URL:
 * /api/medicines
 *
 * ============================================================
 */
@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MedicineController {

    private final MedicineService medicineService;


    // ============================================================
    // CREATE MEDICINE
    // ============================================================

    /**
     * Create a new medicine reminder.
     *
     * POST:
     * /api/medicines
     */
    @PostMapping
    public ResponseEntity<MedicineResponse> createMedicine(
            @RequestBody MedicineRequest request
    ) {

        MedicineResponse response =
                medicineService.createMedicine(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ============================================================
    // GET ONE MEDICINE
    // ============================================================

    /**
     * Get one medicine reminder by ID.
     *
     * GET:
     * /api/medicines/{medicineId}
     */
    @GetMapping("/{medicineId}")
    public ResponseEntity<MedicineResponse> getMedicine(
            @PathVariable Long medicineId
    ) {

        MedicineResponse response =
                medicineService.getMedicine(medicineId);

        return ResponseEntity.ok(response);
    }


    // ============================================================
    // GET ALL PATIENT MEDICINES
    // ============================================================

    /**
     * Get all medicine reminders belonging to a patient.
     *
     * GET:
     * /api/medicines/patient/{accountId}
     */
    @GetMapping("/patient/{accountId}")
    public ResponseEntity<List<MedicineResponse>> getPatientMedicines(
            @PathVariable Long accountId
    ) {

        List<MedicineResponse> medicines =
                medicineService.getPatientMedicines(accountId);

        return ResponseEntity.ok(medicines);
    }


    // ============================================================
    // GET ACTIVE PATIENT MEDICINES
    // ============================================================

    /**
     * Get only active medicine reminders for a patient.
     *
     * The results are ordered by reminder time.
     *
     * GET:
     * /api/medicines/patient/{accountId}/active
     */
    @GetMapping("/patient/{accountId}/active")
    public ResponseEntity<List<MedicineResponse>> getActivePatientMedicines(
            @PathVariable Long accountId
    ) {

        List<MedicineResponse> medicines =
                medicineService.getActivePatientMedicines(accountId);

        return ResponseEntity.ok(medicines);
    }


    // ============================================================
    // UPDATE MEDICINE
    // ============================================================

    /**
     * Update an existing medicine reminder.
     *
     * PUT:
     * /api/medicines/{medicineId}
     */
    @PutMapping("/{medicineId}")
    public ResponseEntity<MedicineResponse> updateMedicine(
            @PathVariable Long medicineId,
            @RequestBody MedicineRequest request
    ) {

        MedicineResponse response =
                medicineService.updateMedicine(
                        medicineId,
                        request
                );

        return ResponseEntity.ok(response);
    }


    // ============================================================
    // DEACTIVATE MEDICINE
    // ============================================================

    /**
     * Deactivate a medicine reminder.
     *
     * The medicine remains in the database but will no longer
     * appear among the patient's active reminders.
     *
     * PUT:
     * /api/medicines/{medicineId}/deactivate
     */
    @PutMapping("/{medicineId}/deactivate")
    public ResponseEntity<MedicineResponse> deactivateMedicine(
            @PathVariable Long medicineId
    ) {

        MedicineResponse response =
                medicineService.deactivateMedicine(medicineId);

        return ResponseEntity.ok(response);
    }


    // ============================================================
    // DELETE MEDICINE
    // ============================================================

    /**
     * Permanently delete a medicine record.
     *
     * DELETE:
     * /api/medicines/{medicineId}
     */
    @DeleteMapping("/{medicineId}")
    public ResponseEntity<Void> deleteMedicine(
            @PathVariable Long medicineId
    ) {

        medicineService.deleteMedicine(medicineId);

        return ResponseEntity.noContent().build();
    }
}