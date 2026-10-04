package com.afyasmart.backend.repository;

import com.afyasmart.backend.entity.MedicineDoseLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * ============================================================
 * AfyaSmart - Medicine Dose Log Repository
 * ============================================================
 */
public interface MedicineDoseLogRepository
        extends JpaRepository<MedicineDoseLog, Long> {

    /**
     * Find a dose log for a specific medicine, date and time.
     */
    Optional<MedicineDoseLog> findByMedicineIdAndScheduledDateAndScheduledTime(
            Long medicineId,
            LocalDate scheduledDate,
            LocalTime scheduledTime
    );

    /**
     * Get all dose logs for a medicine.
     */
    List<MedicineDoseLog> findByMedicineIdOrderByScheduledDateDescScheduledTimeDesc(
            Long medicineId
    );

    /**
     * Get today's doses for a patient's medicines.
     */
    List<MedicineDoseLog> findByMedicineAccountIdAndScheduledDateOrderByScheduledTimeAsc(
            Long accountId,
            LocalDate scheduledDate
    );
}