package com.afyasmart.backend.controller;

import com.afyasmart.backend.common.ApiResponse;
import com.afyasmart.backend.dto.DashboardStatsResponse;
import com.afyasmart.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getStats() {

        return ResponseEntity.ok(

                ApiResponse.<DashboardStatsResponse>builder()
                        .success(true)
                        .message("Dashboard statistics retrieved successfully")
                        .data(
                                dashboardService.getDashboardStats()
                        )
                        .build()

        );

    }

}