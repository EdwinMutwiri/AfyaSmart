package com.afyasmart.backend.repository;

import com.afyasmart.backend.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ============================================================
 * AfyaSmart - Medicine Repository
 * ============================================================
 *
 * Provides database operations for Medicine records.
 *
 * Spring Data JPA automatically implements these methods.
 * ============================================================
 */
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    /**
     * Get all medicines belonging to a specific patient.
     */
    List<Medicine> findByAccountId(Long accountId);


    /**
     * Get only active medicines belonging to a patient.
     */
    List<Medicine> findByAccountIdAndActiveTrue(Long accountId);


    /**
     * Get medicines ordered by reminder time.
     *
     * This will help the frontend display the patient's
     * medicines in chronological reminder order.
     */
    List<Medicine> findByAccountIdAndActiveTrueOrderByReminderTimeAsc(
            Long accountId
    );
}