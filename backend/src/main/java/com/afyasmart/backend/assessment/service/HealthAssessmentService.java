package com.afyasmart.backend.assessment.service;

import com.afyasmart.backend.assessment.dto.HealthAssessmentRequest;
import com.afyasmart.backend.assessment.dto.HealthAssessmentResponse;

import java.util.List;

public interface HealthAssessmentService {

    HealthAssessmentResponse analyze(HealthAssessmentRequest request);

    HealthAssessmentResponse getLatestAssessment(Long accountId);

    List<HealthAssessmentResponse> getAssessmentHistory(Long accountId);

}