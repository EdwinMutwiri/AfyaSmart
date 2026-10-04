package com.afyasmart.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * ============================================================
 * AfyaSmart - Medicine Dose Log
 * ============================================================
 *
 * Records individual scheduled medicine doses.
 *
 * Example:
 *
 * Medicine:
 * Paracetamol
 *
 * Scheduled:
 * 27 September 2026 - 08:00
 *
 * Status:
 * TAKEN
 *
 * This allows AfyaSmart to track medication adherence.
 * ============================================================
 */

@Entity
@Table(
        name = "medicine_dose_logs",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "medicine_id",
                                "scheduled_date",
                                "scheduled_time"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineDoseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Medicine associated with this dose.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "medicine_id",
            nullable = false
    )
    private Medicine medicine;

    /**
     * Date the medicine was scheduled.
     */
    @Column(nullable = false)
    private LocalDate scheduledDate;

    /**
     * Time the medicine was scheduled.
     */
    @Column(nullable = false)
    private LocalTime scheduledTime;

    /**
     * Current status of the dose.
     */
    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    @Builder.Default
    private MedicineDoseStatus status =
            MedicineDoseStatus.PENDING;

    /**
     * Time when the patient confirmed taking the medicine.
     */
    private LocalDateTime takenAt;

    /**
     * Automatically set when the record is created.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null) {
            status = MedicineDoseStatus.PENDING;
        }
    }
}