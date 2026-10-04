package com.afyasmart.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * The patient who owns this medicine.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "account_id",
            nullable = false
    )
    private Account account;

    /*
     * Name of the medicine.
     */
    @Column(
            nullable = false,
            length = 150
    )
    private String medicineName;

    /*
     * Example:
     * 500 mg
     * 10 ml
     * 1 tablet
     */
    @Column(
            nullable = false,
            length = 100
    )
    private String dosage;

    /*
     * Examples:
     *
     * Once daily
     * Twice daily
     * 3 times daily
     * 4 times daily
     * As needed
     */
    @Column(
            nullable = false,
            length = 100
    )
    private String frequency;

    /*
     * Date when the medication starts.
     */
    @Column(nullable = false)
    private LocalDate startDate;

    /*
     * Optional date when the medication ends.
     */
    private LocalDate endDate;

    /*
     * ---------------------------------------------------------
     * MULTIPLE DAILY REMINDER TIMES
     * ---------------------------------------------------------
     *
     * A medicine can now have multiple reminder times.
     *
     * Example:
     *
     * Paracetamol
     *
     * reminderTimes:
     * 07:30
     * 13:00
     * 19:00
     *
     * This allows the system to support:
     *
     * 1 dose/day
     * 2 doses/day
     * 3 doses/day
     * 4 doses/day
     */
    @ElementCollection
    @CollectionTable(
            name = "medicine_reminder_times",
            joinColumns = @JoinColumn(name = "medicine_id")
    )
    @Column(name = "reminder_time")
    @OrderColumn(name = "reminder_order")
    @Builder.Default
    private List<LocalTime> reminderTimes = new ArrayList<>();

    /*
     * ---------------------------------------------------------
     * BACKWARD COMPATIBILITY
     * ---------------------------------------------------------
     *
     * This field is temporarily retained because the previous
     * version of AfyaSmart stored only ONE reminderTime.
     *
     * Existing medicines can therefore continue working while
     * we transition to the new multi-dose system.
     *
     * New medicines will primarily use reminderTimes.
     */
    private LocalTime reminderTime;

    /*
     * Additional instructions.
     *
     * Example:
     * "Take after meals"
     */
    @Column(length = 500)
    private String instructions;

    /*
     * Whether this medicine is currently active.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    /*
     * Date/time when the medicine was created.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /*
     * Automatically set creation date/time.
     */
    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (active == null) {
            active = true;
        }

        /*
         * If an old medicine has only reminderTime,
         * automatically copy it into reminderTimes.
         *
         * This helps us avoid breaking existing medicines.
         */
        if ((reminderTimes == null || reminderTimes.isEmpty())
                && reminderTime != null) {

            reminderTimes = new ArrayList<>();
            reminderTimes.add(reminderTime);
        }
    }

    /*
     * ---------------------------------------------------------
     * BACKWARD COMPATIBILITY BEFORE UPDATE
     * ---------------------------------------------------------
     *
     * If an existing record still has the old reminderTime,
     * make sure reminderTimes contains it.
     */
    @PreUpdate
    protected void onUpdate() {

        if ((reminderTimes == null || reminderTimes.isEmpty())
                && reminderTime != null) {

            reminderTimes = new ArrayList<>();
            reminderTimes.add(reminderTime);
        }
    }
}