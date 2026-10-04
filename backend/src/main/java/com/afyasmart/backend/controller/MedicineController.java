package com.afyasmart.backend.controller;

import com.afyasmart.backend.dto.MedicineDoseResponse;
import com.afyasmart.backend.dto.MedicineRequest;
import com.afyasmart.backend.dto.MedicineResponse;
import com.afyasmart.backend.service.MedicineService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MedicineController {

    private final MedicineService medicineService;

    // =========================================================
    // CREATE MEDICINE
    // =========================================================

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

    // =========================================================
    // GET ONE MEDICINE
    // =========================================================

    @GetMapping("/{medicineId}")
    public ResponseEntity<MedicineResponse> getMedicine(
            @PathVariable Long medicineId
    ) {

        MedicineResponse response =
                medicineService.getMedicine(medicineId);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET ALL MEDICINES FOR A PATIENT
    // =========================================================

    @GetMapping("/patient/{accountId}")
    public ResponseEntity<List<MedicineResponse>> getPatientMedicines(
            @PathVariable Long accountId
    ) {

        List<MedicineResponse> medicines =
                medicineService.getPatientMedicines(accountId);

        return ResponseEntity.ok(medicines);
    }

    // =========================================================
    // GET ACTIVE MEDICINES FOR A PATIENT
    // =========================================================

    @GetMapping("/patient/{accountId}/active")
    public ResponseEntity<List<MedicineResponse>> getActivePatientMedicines(
            @PathVariable Long accountId
    ) {

        List<MedicineResponse> medicines =
                medicineService.getActivePatientMedicines(accountId);

        return ResponseEntity.ok(medicines);
    }

    // =========================================================
    // UPDATE MEDICINE
    // =========================================================

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

    // =========================================================
    // DEACTIVATE MEDICINE
    // =========================================================

    @PutMapping("/{medicineId}/deactivate")
    public ResponseEntity<MedicineResponse> deactivateMedicine(
            @PathVariable Long medicineId
    ) {

        MedicineResponse response =
                medicineService.deactivateMedicine(medicineId);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE MEDICINE
    // =========================================================

    @DeleteMapping("/{medicineId}")
    public ResponseEntity<Void> deleteMedicine(
            @PathVariable Long medicineId
    ) {

        medicineService.deleteMedicine(medicineId);

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // MARK A SPECIFIC DOSE AS TAKEN
    // =========================================================
    //
    // IMPORTANT:
    // We now use doseId instead of medicineId.
    //
    // Example:
    // POST /api/medicines/doses/15/taken
    //
    // This allows a medicine taken 3 times a day to track
    // morning, afternoon and evening doses independently.
    // =========================================================

    @PostMapping("/doses/{doseId}/taken")
    public ResponseEntity<MedicineDoseResponse> markDoseAsTaken(
            @PathVariable Long doseId
    ) {

        MedicineDoseResponse response =
                medicineService.markDoseAsTaken(doseId);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // GET TODAY'S MEDICINE DOSES
    // =========================================================
    //
    // Example:
    // GET /api/medicines/patient/1/doses/today
    //
    // This will generate/return each scheduled dose for today.
    // =========================================================

    @GetMapping("/patient/{accountId}/doses/today")
    public ResponseEntity<List<MedicineDoseResponse>> getTodayDoses(
            @PathVariable Long accountId
    ) {

        List<MedicineDoseResponse> doses =
                medicineService.getTodayDoses(accountId);

        return ResponseEntity.ok(doses);
    }

    // =========================================================
    // GET DOSE HISTORY FOR A MEDICINE
    // =========================================================

    @GetMapping("/{medicineId}/dose-history")
    public ResponseEntity<List<MedicineDoseResponse>> getMedicineDoseHistory(
            @PathVariable Long medicineId
    ) {

        List<MedicineDoseResponse> history =
                medicineService.getMedicineDoseHistory(medicineId);

        return ResponseEntity.ok(history);
    }
}