package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.dto.MedicineRequest;
import com.afyasmart.backend.dto.MedicineResponse;
import com.afyasmart.backend.entity.Account;
import com.afyasmart.backend.entity.Medicine;
import com.afyasmart.backend.repository.AccountRepository;
import com.afyasmart.backend.repository.MedicineRepository;
import com.afyasmart.backend.service.MedicineService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ============================================================
 * AfyaSmart - Medicine Service Implementation
 * ============================================================
 *
 * Contains the business logic for medicine reminders.
 *
 * ============================================================
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    private final AccountRepository accountRepository;


    // ============================================================
    // CREATE MEDICINE
    // ============================================================

    @Override
    public MedicineResponse createMedicine(
            MedicineRequest request
    ) {

        validateRequest(request);

        Account account = accountRepository
                .findById(request.getAccountId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient account not found."
                        )
                );

        Medicine medicine = Medicine.builder()
                .account(account)
                .medicineName(request.getMedicineName())
                .dosage(request.getDosage())
                .frequency(request.getFrequency())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reminderTime(request.getReminderTime())
                .instructions(request.getInstructions())
                .active(true)
                .build();

        Medicine savedMedicine =
                medicineRepository.save(medicine);

        return mapToResponse(savedMedicine);
    }


    // ============================================================
    // GET MEDICINE
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public MedicineResponse getMedicine(
            Long medicineId
    ) {

        Medicine medicine = medicineRepository
                .findById(medicineId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Medicine record not found."
                        )
                );

        return mapToResponse(medicine);
    }


    // ============================================================
    // GET PATIENT MEDICINES
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponse> getPatientMedicines(
            Long accountId
    ) {

        return medicineRepository
                .findByAccountId(accountId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ============================================================
    // GET ACTIVE PATIENT MEDICINES
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponse> getActivePatientMedicines(
            Long accountId
    ) {

        return medicineRepository
                .findByAccountIdAndActiveTrueOrderByReminderTimeAsc(
                        accountId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ============================================================
    // UPDATE MEDICINE
    // ============================================================

    @Override
    public MedicineResponse updateMedicine(
            Long medicineId,
            MedicineRequest request
    ) {

        validateRequest(request);

        Medicine medicine = medicineRepository
                .findById(medicineId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Medicine record not found."
                        )
                );

        /*
         * We allow the medicine details to be updated while
         * keeping the original patient account relationship.
         */
        medicine.setMedicineName(
                request.getMedicineName()
        );

        medicine.setDosage(
                request.getDosage()
        );

        medicine.setFrequency(
                request.getFrequency()
        );

        medicine.setStartDate(
                request.getStartDate()
        );

        medicine.setEndDate(
                request.getEndDate()
        );

        medicine.setReminderTime(
                request.getReminderTime()
        );

        medicine.setInstructions(
                request.getInstructions()
        );

        Medicine updatedMedicine =
                medicineRepository.save(medicine);

        return mapToResponse(updatedMedicine);
    }


    // ============================================================
    // DEACTIVATE MEDICINE
    // ============================================================

    @Override
    public MedicineResponse deactivateMedicine(
            Long medicineId
    ) {

        Medicine medicine = medicineRepository
                .findById(medicineId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Medicine record not found."
                        )
                );

        medicine.setActive(false);

        Medicine updatedMedicine =
                medicineRepository.save(medicine);

        return mapToResponse(updatedMedicine);
    }


    // ============================================================
    // DELETE MEDICINE
    // ============================================================

    @Override
    public void deleteMedicine(
            Long medicineId
    ) {

        if (!medicineRepository.existsById(medicineId)) {

            throw new RuntimeException(
                    "Medicine record not found."
            );
        }

        medicineRepository.deleteById(medicineId);
    }


    // ============================================================
    // MAP ENTITY TO RESPONSE
    // ============================================================

    private MedicineResponse mapToResponse(
            Medicine medicine
    ) {

        Account account =
                medicine.getAccount();

        String patientName =
                buildPatientName(account);

        return MedicineResponse.builder()
                .id(medicine.getId())
                .accountId(account.getId())
                .patientName(patientName)
                .medicineName(medicine.getMedicineName())
                .dosage(medicine.getDosage())
                .frequency(medicine.getFrequency())
                .startDate(medicine.getStartDate())
                .endDate(medicine.getEndDate())
                .reminderTime(medicine.getReminderTime())
                .instructions(medicine.getInstructions())
                .active(medicine.getActive())
                .createdAt(medicine.getCreatedAt())
                .build();
    }


    // ============================================================
    // BUILD PATIENT NAME
    // ============================================================

    private String buildPatientName(
            Account account
    ) {

        String firstName =
                account.getFirstName() == null
                        ? ""
                        : account.getFirstName().trim();

        String lastName =
                account.getLastName() == null
                        ? ""
                        : account.getLastName().trim();

        String fullName =
                (firstName + " " + lastName).trim();

        if (fullName.isEmpty()) {
            return account.getEmail();
        }

        return fullName;
    }


    // ============================================================
    // VALIDATION
    // ============================================================

    private void validateRequest(
            MedicineRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Medicine request cannot be null."
            );
        }

        if (request.getAccountId() == null) {
            throw new IllegalArgumentException(
                    "Patient account ID is required."
            );
        }

        if (isBlank(request.getMedicineName())) {
            throw new IllegalArgumentException(
                    "Medicine name is required."
            );
        }

        if (isBlank(request.getDosage())) {
            throw new IllegalArgumentException(
                    "Dosage is required."
            );
        }

        if (isBlank(request.getFrequency())) {
            throw new IllegalArgumentException(
                    "Medicine frequency is required."
            );
        }

        if (request.getStartDate() == null) {
            throw new IllegalArgumentException(
                    "Medicine start date is required."
            );
        }

        if (
                request.getEndDate() != null &&
                        request.getEndDate()
                                .isBefore(request.getStartDate())
        ) {

            throw new IllegalArgumentException(
                    "Medicine end date cannot be before start date."
            );
        }
    }


    /**
     * Checks whether a String is null, empty, or whitespace.
     */
    private boolean isBlank(String value) {

        return value == null ||
                value.trim().isEmpty();
    }
}