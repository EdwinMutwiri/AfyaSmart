package com.afyasmart.backend.telemedicine.entity;

import com.afyasmart.backend.entity.Appointment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "telemedicine_consultations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelemedicineConsultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Links the telemedicine consultation to an existing appointment.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    /**
     * Meeting URL used by the patient and doctor.
     */
    @Column(length = 500)
    private String meetingLink;

    /**
     * PENDING, READY, IN_PROGRESS, COMPLETED, CANCELLED
     */
    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "PENDING";

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;
}