package com.afyasmart.backend.repository;

import com.afyasmart.backend.ai.HealthTip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthTipRepository
        extends JpaRepository<HealthTip, Long> {

    List<HealthTip> findByActiveTrue();

    List<HealthTip> findByCategoryAndActiveTrue(String category);

    List<HealthTip> findByConditionTagAndActiveTrue(String conditionTag);
}