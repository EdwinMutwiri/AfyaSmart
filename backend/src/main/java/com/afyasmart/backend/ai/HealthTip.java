package com.afyasmart.backend.ai;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_tips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthTip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Short title displayed to the patient.
     */
    @Column(nullable = false, length = 150)
    private String title;

    /**
     * Main health advice.
     */
    @Column(nullable = false, length = 1000)
    private String content;

    /**
     * Category such as:
     * HYDRATION, NUTRITION, EXERCISE, SLEEP,
     * MEDICATION, MENTAL_WELLBEING or PREVENTION.
     */
    @Column(nullable = false, length = 50)
    private String category;

    /**
     * Optional tag that can later be used
     * for personalized recommendations.
     */
    @Column(length = 100)
    private String conditionTag;

    /**
     * Allows administrators to disable a tip
     * without deleting it.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (active == null) {
            active = true;
        }
    }
}