package com.afyasmart.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Login account
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    // Medical specialization
    @ManyToOne
    @JoinColumn(name = "specialization_id", nullable = false)
    private Specialization specialization;

    @Column(nullable = false)
    private String hospital;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    private Integer yearsExperience;

    private BigDecimal consultationFee;

    @Column(length = 1000)
    private String bio;

    @Builder.Default
    private Boolean available = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}