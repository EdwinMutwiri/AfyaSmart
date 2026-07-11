package com.afyasmart.backend.assessment.repository;

import com.afyasmart.backend.assessment.entity.HealthAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthAssessmentRepository extends JpaRepository<HealthAssessment, Long> {

    List<HealthAssessment> findByAccountIdOrderByAssessmentDateDesc(Long accountId);

}