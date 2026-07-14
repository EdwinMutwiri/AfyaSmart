package com.afyasmart.backend.controller;

import com.afyasmart.backend.common.ApiResponse;
import com.afyasmart.backend.dto.AdminDashboardResponse;
import com.afyasmart.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> dashboard() {

        return ResponseEntity.ok(

                ApiResponse.<AdminDashboardResponse>builder()
                        .success(true)
                        .message("Dashboard loaded successfully")
                        .data(adminService.getDashboardStatistics())
                        .build()

        );

    }

}