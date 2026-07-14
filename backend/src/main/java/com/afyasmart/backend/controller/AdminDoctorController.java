package com.afyasmart.backend.controller;

import com.afyasmart.backend.common.ApiResponse;
import com.afyasmart.backend.dto.CreateDoctorRequest;
import com.afyasmart.backend.dto.DoctorProfileResponse;
import com.afyasmart.backend.service.AdminDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/doctors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminDoctorController {

    private final AdminDoctorService adminDoctorService;

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorProfileResponse>> createDoctor(
            @RequestBody CreateDoctorRequest request) {

        DoctorProfileResponse doctor =
                adminDoctorService.createDoctor(request);

        return ResponseEntity.ok(
                ApiResponse.<DoctorProfileResponse>builder()
                        .success(true)
                        .message("Doctor created successfully")
                        .data(doctor)
                        .build()
        );
    }
}