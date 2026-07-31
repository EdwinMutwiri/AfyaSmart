package com.afyasmart.backend.controller;

import com.afyasmart.backend.common.ApiResponse;
import com.afyasmart.backend.dto.DashboardAnalyticsResponse;
import com.afyasmart.backend.service.DashboardAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardAnalyticsController {

    private final DashboardAnalyticsService dashboardAnalyticsService;

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<DashboardAnalyticsResponse>>
    getAnalytics() {

        return ResponseEntity.ok(

                ApiResponse.<DashboardAnalyticsResponse>builder()
                        .success(true)
                        .message("Dashboard analytics retrieved successfully")
                        .data(
                                dashboardAnalyticsService.getAnalytics()
                        )
                        .build()

        );

    }

}