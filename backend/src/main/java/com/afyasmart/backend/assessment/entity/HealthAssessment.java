package com.afyasmart.backend.assessment.entity;

import com.afyasmart.backend.entity.Account;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private Integer age;

    private String gender;

    private Double height;

    private Double weight;

    private Integer systolic;

    private Integer diastolic;

    private Double bloodSugar;

    private Integer exerciseDays;

    private Boolean smoker;

    private Boolean alcohol;

    private Double bmi;

    private Integer healthScore;

    private String riskLevel;

    @Column(length = 2000)
    private String recommendation;

    @Builder.Default
    private LocalDateTime assessmentDate = LocalDateTime.now();
}