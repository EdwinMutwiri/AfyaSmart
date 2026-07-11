package com.afyasmart.backend.assessment.service;

import com.afyasmart.backend.assessment.dto.HealthAssessmentRequest;
import com.afyasmart.backend.assessment.dto.HealthAssessmentResponse;

public interface HealthAssessmentService {

    HealthAssessmentResponse analyze(HealthAssessmentRequest request);

}