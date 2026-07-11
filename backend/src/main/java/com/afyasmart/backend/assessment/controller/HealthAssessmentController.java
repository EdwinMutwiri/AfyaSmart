package com.afyasmart.backend.assessment.controller;

import com.afyasmart.backend.assessment.dto.HealthAssessmentRequest;
import com.afyasmart.backend.assessment.dto.HealthAssessmentResponse;
import com.afyasmart.backend.assessment.service.HealthAssessmentService;
import com.afyasmart.backend.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assessment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HealthAssessmentController {

    private final HealthAssessmentService service;

    @PostMapping
    public ResponseEntity<ApiResponse<HealthAssessmentResponse>> analyze(
            @Valid @RequestBody HealthAssessmentRequest request) {

        return ResponseEntity.ok(

                ApiResponse.<HealthAssessmentResponse>builder()

                        .success(true)

                        .message("Assessment completed successfully")

                        .data(service.analyze(request))

                        .build()

        );

    }

}