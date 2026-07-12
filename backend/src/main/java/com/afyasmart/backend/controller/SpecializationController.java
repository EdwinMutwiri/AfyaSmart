package com.afyasmart.backend.controller;

import com.afyasmart.backend.common.ApiResponse;
import com.afyasmart.backend.entity.Specialization;
import com.afyasmart.backend.service.SpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specializations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SpecializationController {

    private final SpecializationService specializationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Specialization>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.<List<Specialization>>builder()
                        .success(true)
                        .message("Specializations retrieved successfully")
                        .data(specializationService.getAllSpecializations())
                        .build()
        );
    }
}