package com.afyasmart.backend.service;

import com.afyasmart.backend.dto.HealthTipRecommendationRequest;
import com.afyasmart.backend.dto.HealthTipRequest;
import com.afyasmart.backend.dto.HealthTipResponse;

import java.util.List;

public interface HealthTipService {

    HealthTipResponse createHealthTip(HealthTipRequest request);

    List<HealthTipResponse> getAllActiveTips();

    HealthTipResponse getDailyTip();

    /*
     * Recommends a health tip using manually supplied
     * health information.
     */
    HealthTipResponse getRecommendedTip(
            HealthTipRecommendationRequest request
    );

    /*
     * Recommends a health tip automatically using
     * the patient's latest health assessment.
     *
     * The frontend only needs to provide the
     * patient's account ID.
     */
    HealthTipResponse getRecommendedTipForPatient(
            Long accountId
    );

    List<HealthTipResponse> getTipsByCategory(
            String category
    );

    List<HealthTipResponse> getTipsByCondition(
            String conditionTag
    );

    HealthTipResponse getHealthTip(Long id);

    HealthTipResponse deactivateHealthTip(Long id);

    void deleteHealthTip(Long id);
}