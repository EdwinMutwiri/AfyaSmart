package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.dto.MedicineDoseResponse;
import com.afyasmart.backend.dto.MedicineRequest;
import com.afyasmart.backend.dto.MedicineResponse;
import com.afyasmart.backend.entity.Account;
import com.afyasmart.backend.entity.Medicine;
import com.afyasmart.backend.entity.MedicineDoseLog;
import com.afyasmart.backend.entity.MedicineDoseStatus;
import com.afyasmart.backend.repository.AccountRepository;
import com.afyasmart.backend.repository.MedicineDoseLogRepository;
import com.afyasmart.backend.repository.MedicineRepository;
import com.afyasmart.backend.service.MedicineService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * ============================================================
 * AfyaSmart - Medicine Service Implementation
 * ============================================================
 *
 * Handles:
 *
 * 1. Creating medicines
 * 2. Updating medicines
 * 3. Retrieving medicines
 * 4. Deactivating medicines
 * 5. Deleting medicines
 * 6. Creating today's scheduled doses
 * 7. Marking individual doses as TAKEN
 * 8. Retrieving dose history
 *
 * MULTI-DOSE SUPPORT
 * ------------------
 *
 * A single medicine can now have several reminder times.
 *
 * Example:
 *
 * Paracetamol
 *
 * 07:30
 * 13:00
 * 19:00
 *
 * Each time becomes an independent dose record.
 * ============================================================
 */
@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {


    /*
     * Repository for medicines.
     */
    private final MedicineRepository medicineRepository;


    /*
     * Repository for patient accounts.
     */
    private final AccountRepository accountRepository;


    /*
     * Repository for individual dose records.
     */
    private final MedicineDoseLogRepository medicineDoseLogRepository;


    // =========================================================
    // CREATE MEDICINE
    // =========================================================

    @Override
    public MedicineResponse createMedicine(
            MedicineRequest request
    ) {

        validateRequest(request);

        /*
         * Find the patient account.
         */
        Account account = accountRepository
                .findById(request.getAccountId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient account not found."
                        )
                );


        /*
         * Get reminder times from request.
         */
        List<LocalTime> reminderTimes =
                normalizeReminderTimes(
                        request.getReminderTimes()
                );


        /*
         * If the frontend did not send the new
         * reminderTimes field, we cannot create
         * scheduled reminders.
         */
        if (reminderTimes.isEmpty()) {

            throw new RuntimeException(
                    "At least one reminder time is required."
            );
        }


        /*
         * Build medicine.
         */
        Medicine medicine = Medicine.builder()

                .account(account)

                .medicineName(
                        request.getMedicineName()
                )

                .dosage(
                        request.getDosage()
                )

                .frequency(
                        request.getFrequency()
                )

                .startDate(
                        request.getStartDate()
                )

                .endDate(
                        request.getEndDate()
                )

                .reminderTimes(
                        reminderTimes
                )

                /*
                 * Keep the old field populated with the
                 * first reminder time for compatibility.
                 */
                .reminderTime(
                        reminderTimes.get(0)
                )

                .instructions(
                        request.getInstructions()
                )

                .active(true)

                .build();


        /*
         * Save medicine.
         */
        Medicine savedMedicine =
                medicineRepository.save(medicine);


        return mapToResponse(savedMedicine);
    }


    // =========================================================
    // GET SINGLE MEDICINE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public MedicineResponse getMedicine(
            Long medicineId
    ) {

        Medicine medicine =
                medicineRepository
                        .findById(medicineId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Medicine not found."
                                )
                        );

        return mapToResponse(medicine);
    }


    // =========================================================
    // GET ALL PATIENT MEDICINES
    // =========================================================

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


    // =========================================================
    // GET ACTIVE PATIENT MEDICINES
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponse> getActivePatientMedicines(
            Long accountId
    ) {

        return medicineRepository
                .findByAccountIdAndActiveTrue(accountId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // UPDATE MEDICINE
    // =========================================================

    @Override
    public MedicineResponse updateMedicine(
            Long medicineId,
            MedicineRequest request
    ) {

        validateRequest(request);

        Medicine medicine =
                medicineRepository
                        .findById(medicineId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Medicine not found."
                                )
                        );


        List<LocalTime> reminderTimes =
                normalizeReminderTimes(
                        request.getReminderTimes()
                );


        if (reminderTimes.isEmpty()) {

            throw new RuntimeException(
                    "At least one reminder time is required."
            );
        }


        /*
         * Update medicine information.
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

        medicine.setReminderTimes(
                reminderTimes
        );

        /*
         * Keep the old field synchronized with the
         * first reminder time.
         */
        medicine.setReminderTime(
                reminderTimes.get(0)
        );

        medicine.setInstructions(
                request.getInstructions()
        );


        Medicine updatedMedicine =
                medicineRepository.save(medicine);


        return mapToResponse(updatedMedicine);
    }


    // =========================================================
    // DEACTIVATE MEDICINE
    // =========================================================

    @Override
    public MedicineResponse deactivateMedicine(
            Long medicineId
    ) {

        Medicine medicine =
                medicineRepository
                        .findById(medicineId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Medicine not found."
                                )
                        );


        medicine.setActive(false);


        Medicine updatedMedicine =
                medicineRepository.save(medicine);


        return mapToResponse(updatedMedicine);
    }


    // =========================================================
    // DELETE MEDICINE
    // =========================================================

    @Override
    public void deleteMedicine(
            Long medicineId
    ) {

        Medicine medicine =
                medicineRepository
                        .findById(medicineId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Medicine not found."
                                )
                        );


        medicineRepository.delete(medicine);
    }


    // =========================================================
    // MARK INDIVIDUAL DOSE AS TAKEN
    // =========================================================

    @Override
    public MedicineDoseResponse markDoseAsTaken(
            Long doseId
    ) {

        /*
         * Find the exact dose.
         *
         * This is important because a medicine may have
         * several doses today.
         */
        MedicineDoseLog dose =
                medicineDoseLogRepository
                        .findById(doseId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Medicine dose not found."
                                )
                        );


        /*
         * Ensure the medicine is active.
         */
        if (!Boolean.TRUE.equals(
                dose.getMedicine().getActive()
        )) {

            throw new RuntimeException(
                    "This medicine is no longer active."
            );
        }


        /*
         * Mark ONLY this dose as taken.
         */
        dose.setStatus(
                MedicineDoseStatus.TAKEN
        );


        dose.setTakenAt(
                LocalDateTime.now()
        );


        MedicineDoseLog savedDose =
                medicineDoseLogRepository.save(dose);


        return mapDoseToResponse(savedDose);
    }


    // =========================================================
    // GET TODAY'S DOSES
    // =========================================================

    @Override
    @Transactional
    public List<MedicineDoseResponse> getTodayDoses(
            Long accountId
    ) {

        LocalDate today =
                LocalDate.now();


        /*
         * Find all active medicines belonging to the patient.
         */
        List<Medicine> medicines =
                medicineRepository
                        .findByAccountIdAndActiveTrue(
                                accountId
                        );


        /*
         * We will build today's dose list here.
         */
        List<MedicineDoseLog> todayDoseLogs =
                new ArrayList<>();


        for (Medicine medicine : medicines) {

            /*
             * Do not generate doses before the medication
             * start date.
             */
            if (today.isBefore(
                    medicine.getStartDate()
            )) {
                continue;
            }


            /*
             * Do not generate doses after the medication
             * end date.
             */
            if (medicine.getEndDate() != null
                    && today.isAfter(
                    medicine.getEndDate()
            )) {
                continue;
            }


            /*
             * Get the medicine's reminder times.
             */
            List<LocalTime> reminderTimes =
                    getEffectiveReminderTimes(
                            medicine
                    );


            /*
             * Create one dose record for every reminder time.
             */
            for (LocalTime reminderTime :
                    reminderTimes) {


                /*
                 * Check whether today's dose already exists.
                 */
                MedicineDoseLog dose =
                        medicineDoseLogRepository
                                .findByMedicineIdAndScheduledDateAndScheduledTime(
                                        medicine.getId(),
                                        today,
                                        reminderTime
                                )
                                .orElse(null);


                /*
                 * If it does not exist, create it.
                 */
                if (dose == null) {

                    dose = MedicineDoseLog
                            .builder()

                            .medicine(medicine)

                            .scheduledDate(today)

                            .scheduledTime(
                                    reminderTime
                            )

                            .status(
                                    MedicineDoseStatus.PENDING
                            )

                            .build();


                    dose =
                            medicineDoseLogRepository
                                    .save(dose);
                }


                todayDoseLogs.add(dose);
            }
        }


        /*
         * Sort today's doses by scheduled time.
         *
         * Example:
         *
         * 07:30
         * 13:00
         * 19:00
         */
        todayDoseLogs.sort(
                Comparator.comparing(
                        MedicineDoseLog::getScheduledTime
                )
        );


        return todayDoseLogs
                .stream()
                .map(this::mapDoseToResponse)
                .toList();
    }


    // =========================================================
    // DOSE HISTORY
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<MedicineDoseResponse> getMedicineDoseHistory(
            Long medicineId
    ) {

        return medicineDoseLogRepository
                .findByMedicineIdOrderByScheduledDateDescScheduledTimeDesc(
                        medicineId
                )
                .stream()
                .map(this::mapDoseToResponse)
                .toList();
    }


    // =========================================================
    // VALIDATE MEDICINE REQUEST
    // =========================================================

    private void validateRequest(
            MedicineRequest request
    ) {

        if (request == null) {

            throw new RuntimeException(
                    "Medicine request cannot be empty."
            );
        }


        if (request.getAccountId() == null) {

            throw new RuntimeException(
                    "Account ID is required."
            );
        }


        if (isBlank(
                request.getMedicineName()
        )) {

            throw new RuntimeException(
                    "Medicine name is required."
            );
        }


        if (isBlank(
                request.getDosage()
        )) {

            throw new RuntimeException(
                    "Dosage is required."
            );
        }


        if (isBlank(
                request.getFrequency()
        )) {

            throw new RuntimeException(
                    "Frequency is required."
            );
        }


        if (request.getStartDate() == null) {

            throw new RuntimeException(
                    "Start date is required."
            );
        }


        if (request.getEndDate() != null
                && request.getEndDate()
                .isBefore(
                        request.getStartDate()
                )) {

            throw new RuntimeException(
                    "End date cannot be before start date."
            );
        }
    }


    // =========================================================
    // NORMALIZE REMINDER TIMES
    // =========================================================

    private List<LocalTime> normalizeReminderTimes(
            List<LocalTime> reminderTimes
    ) {

        if (reminderTimes == null) {
            return new ArrayList<>();
        }


        return reminderTimes
                .stream()

                .filter(time -> time != null)

                .distinct()

                .sorted()

                .toList();
    }


    // =========================================================
    // GET EFFECTIVE REMINDER TIMES
    // =========================================================

    private List<LocalTime> getEffectiveReminderTimes(
            Medicine medicine
    ) {

        /*
         * New medicines use reminderTimes.
         */
        if (medicine.getReminderTimes() != null
                && !medicine.getReminderTimes().isEmpty()) {

            return medicine
                    .getReminderTimes()
                    .stream()
                    .filter(time -> time != null)
                    .distinct()
                    .sorted()
                    .toList();
        }


        /*
         * Existing medicines created under the old system
         * only have reminderTime.
         *
         * Use it as one scheduled dose.
         */
        if (medicine.getReminderTime() != null) {

            return List.of(
                    medicine.getReminderTime()
            );
        }


        return List.of();
    }


    // =========================================================
    // MAP MEDICINE TO RESPONSE
    // =========================================================

    private MedicineResponse mapToResponse(
            Medicine medicine
    ) {

        List<LocalTime> reminderTimes =
                getEffectiveReminderTimes(
                        medicine
                );


        return MedicineResponse
                .builder()

                .id(
                        medicine.getId()
                )

                .accountId(
                        medicine.getAccount().getId()
                )

                .patientName(
                        buildPatientName(
                                medicine.getAccount()
                        )
                )

                .medicineName(
                        medicine.getMedicineName()
                )

                .dosage(
                        medicine.getDosage()
                )

                .frequency(
                        medicine.getFrequency()
                )

                .startDate(
                        medicine.getStartDate()
                )

                .endDate(
                        medicine.getEndDate()
                )

                .reminderTimes(
                        reminderTimes
                )

                /*
                 * Keep first reminder available for
                 * older frontend code.
                 */
                .reminderTime(
                        reminderTimes.isEmpty()
                                ? null
                                : reminderTimes.get(0)
                )

                .instructions(
                        medicine.getInstructions()
                )

                .active(
                        medicine.getActive()
                )

                .createdAt(
                        medicine.getCreatedAt()
                )

                .build();
    }


    // =========================================================
    // MAP DOSE TO RESPONSE
    // =========================================================

    private MedicineDoseResponse mapDoseToResponse(
            MedicineDoseLog dose
    ) {

        Medicine medicine =
                dose.getMedicine();


        return MedicineDoseResponse
                .builder()

                .id(
                        dose.getId()
                )

                .medicineId(
                        medicine.getId()
                )

                .medicineName(
                        medicine.getMedicineName()
                )

                .dosage(
                        medicine.getDosage()
                )

                .scheduledDate(
                        dose.getScheduledDate()
                )

                .scheduledTime(
                        dose.getScheduledTime()
                )

                .status(
                        dose.getStatus()
                )

                .takenAt(
                        dose.getTakenAt()
                )

                .build();
    }


    // =========================================================
    // BUILD PATIENT NAME
    // =========================================================

    private String buildPatientName(
            Account account
    ) {

        String firstName =
                account.getFirstName();

        String lastName =
                account.getLastName();


        String fullName =
                ((firstName != null)
                        ? firstName.trim()
                        : "")
                        + " "
                        + ((lastName != null)
                        ? lastName.trim()
                        : "");


        fullName =
                fullName.trim();


        if (!fullName.isEmpty()) {
            return fullName;
        }


        return account.getEmail();
    }


    // =========================================================
    // STRING VALIDATION HELPER
    // =========================================================

    private boolean isBlank(
            String value
    ) {

        return value == null
                || value.trim().isEmpty();
    }
}