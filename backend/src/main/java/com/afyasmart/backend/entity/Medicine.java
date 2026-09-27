package com.afyasmart.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * ============================================================
 * AfyaSmart - Medicine Entity
 * ============================================================
 *
 * Stores medicine/reminder information for a patient.
 *
 * Each medicine record belongs to one Account (patient).
 *
 * The entity stores:
 *
 * - Medicine name
 * - Dosage
 * - Frequency
 * - Start date
 * - End date
 * - Reminder time
 * - Instructions
 * - Active status
 * - Creation timestamp
 *
 * ============================================================
 */

@Entity
@Table(name = "medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * Patient who owns this medicine reminder.
     *
     * The account_id column links this medicine record
     * to the patient's account.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "account_id",
            nullable = false
    )
    private Account account;


    /**
     * Name of the medicine.
     *
     * Example:
     * Amoxicillin
     * Paracetamol
     * Ibuprofen
     */
    @Column(
            nullable = false,
            length = 150
    )
    private String medicineName;


    /**
     * Dosage information.
     *
     * Example:
     * 500 mg
     * 2 tablets
     * 10 ml
     */
    @Column(
            nullable = false,
            length = 100
    )
    private String dosage;


    /**
     * How frequently the medicine should be taken.
     *
     * Example:
     * Once daily
     * Twice daily
     * Three times daily
     * As needed
     */
    @Column(
            nullable = false,
            length = 100
    )
    private String frequency;


    /**
     * Date when the medicine schedule begins.
     */
    @Column(nullable = false)
    private LocalDate startDate;


    /**
     * Date when the medicine schedule ends.
     *
     * This can be null for medicines without a fixed
     * end date.
     */
    private LocalDate endDate;


    /**
     * Time when the reminder should be triggered.
     *
     * This first version supports one primary reminder time
     * per medicine record.
     */
    private LocalTime reminderTime;


    /**
     * Additional instructions for taking the medicine.
     *
     * Example:
     * "Take after meals"
     * "Take with plenty of water"
     */
    @Column(length = 500)
    private String instructions;


    /**
     * Determines whether this medicine reminder is currently
     * active.
     *
     * New medicine records are active by default.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;


    /**
     * Date and time when the medicine record was created.
     */
    @Column(nullable = false)
    private java.time.LocalDateTime createdAt;


    /**
     * Automatically set the creation timestamp before the
     * entity is inserted into the database.
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = java.time.LocalDateTime.now();
        }

        if (active == null) {
            active = true;
        }
    }
}