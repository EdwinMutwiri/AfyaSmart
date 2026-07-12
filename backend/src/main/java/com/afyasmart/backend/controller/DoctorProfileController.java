package com.afyasmart.backend.controller;

import com.afyasmart.backend.common.ApiResponse;
import com.afyasmart.backend.dto.DoctorProfileRequest;
import com.afyasmart.backend.dto.DoctorProfileResponse;
import com.afyasmart.backend.service.DoctorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DoctorProfileController {

    private final DoctorProfileService doctorService;

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorProfileResponse>> createDoctor(
            @RequestBody DoctorProfileRequest request) {

        DoctorProfileResponse response = doctorService.createDoctor(request);

        return ResponseEntity.ok(
                ApiResponse.<DoctorProfileResponse>builder()
                        .success(true)
                        .message("Doctor created successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorProfileResponse>>> getAllDoctors() {

        return ResponseEntity.ok(
                ApiResponse.<List<DoctorProfileResponse>>builder()
                        .success(true)
                        .message("Doctors retrieved successfully")
                        .data(doctorService.getAllDoctors())
                        .build()
        );
    }

    @GetMapping("/specialization/{id}")
    public ResponseEntity<ApiResponse<List<DoctorProfileResponse>>> getDoctorsBySpecialization(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<List<DoctorProfileResponse>>builder()
                        .success(true)
                        .message("Doctors retrieved successfully")
                        .data(doctorService.getDoctorsBySpecialization(id))
                        .build()
        );
    }
}