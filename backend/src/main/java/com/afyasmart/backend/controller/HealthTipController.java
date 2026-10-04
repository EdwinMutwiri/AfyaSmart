package com.afyasmart.backend.controller;

import com.afyasmart.backend.common.ApiResponse;
import com.afyasmart.backend.dto.HealthTipRecommendationRequest;
import com.afyasmart.backend.dto.HealthTipRequest;
import com.afyasmart.backend.dto.HealthTipResponse;
import com.afyasmart.backend.service.HealthTipService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/health-tips")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HealthTipController {


    private final HealthTipService healthTipService;


    // ============================================================
    // CREATE HEALTH TIP
    // ============================================================

    @PostMapping
    public ResponseEntity<ApiResponse<HealthTipResponse>>
    createHealthTip(
            @Valid @RequestBody HealthTipRequest request) {

        return ResponseEntity.ok(

                ApiResponse.<HealthTipResponse>builder()

                        .success(true)

                        .message(
                                "Health tip created successfully"
                        )

                        .data(
                                healthTipService
                                        .createHealthTip(request)
                        )

                        .build()
        );
    }


    // ============================================================
    // GET ALL ACTIVE HEALTH TIPS
    // ============================================================

    @GetMapping
    public ResponseEntity<ApiResponse<List<HealthTipResponse>>>
    getAllActiveTips() {

        return ResponseEntity.ok(

                ApiResponse.<List<HealthTipResponse>>builder()

                        .success(true)

                        .message(
                                "Active health tips retrieved successfully"
                        )

                        .data(
                                healthTipService
                                        .getAllActiveTips()
                        )

                        .build()
        );
    }


    // ============================================================
    // GET DAILY TIP
    // ============================================================

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<HealthTipResponse>>
    getDailyTip() {

        return ResponseEntity.ok(

                ApiResponse.<HealthTipResponse>builder()

                        .success(true)

                        .message(
                                "Daily health tip retrieved successfully"
                        )

                        .data(
                                healthTipService
                                        .getDailyTip()
                        )

                        .build()
        );
    }


    // ============================================================
    // AUTOMATIC PATIENT RECOMMENDATION
    // ============================================================

    /*
     * This is the important new endpoint.
     *
     * The frontend only supplies accountId.
     *
     * Backend:
     *
     * accountId
     *     ↓
     * latest assessment
     *     ↓
     * BMI + risk + recommendation
     *     ↓
     * recommendation engine
     *     ↓
     * personalized health tip
     */
    @GetMapping("/recommend/{accountId}")
    public ResponseEntity<ApiResponse<HealthTipResponse>>
    getRecommendedTipForPatient(
            @PathVariable Long accountId) {

        return ResponseEntity.ok(

                ApiResponse.<HealthTipResponse>builder()

                        .success(true)

                        .message(
                                "Personalized health tip generated successfully"
                        )

                        .data(
                                healthTipService
                                        .getRecommendedTipForPatient(
                                                accountId
                                        )
                        )

                        .build()
        );
    }


    // ============================================================
    // MANUAL RECOMMENDATION
    // ============================================================

    @PostMapping("/recommend")
    public ResponseEntity<ApiResponse<HealthTipResponse>>
    getRecommendedTip(
            @RequestBody HealthTipRecommendationRequest request) {

        return ResponseEntity.ok(

                ApiResponse.<HealthTipResponse>builder()

                        .success(true)

                        .message(
                                "Health tip recommendation generated successfully"
                        )

                        .data(
                                healthTipService
                                        .getRecommendedTip(request)
                        )

                        .build()
        );
    }


    // ============================================================
    // GET TIPS BY CATEGORY
    // ============================================================

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<HealthTipResponse>>>
    getTipsByCategory(
            @PathVariable String category) {

        return ResponseEntity.ok(

                ApiResponse.<List<HealthTipResponse>>builder()

                        .success(true)

                        .message(
                                "Health tips retrieved successfully"
                        )

                        .data(
                                healthTipService
                                        .getTipsByCategory(category)
                        )

                        .build()
        );
    }


    // ============================================================
    // GET TIPS BY CONDITION
    // ============================================================

    @GetMapping("/condition/{conditionTag}")
    public ResponseEntity<ApiResponse<List<HealthTipResponse>>>
    getTipsByCondition(
            @PathVariable String conditionTag) {

        return ResponseEntity.ok(

                ApiResponse.<List<HealthTipResponse>>builder()

                        .success(true)

                        .message(
                                "Health tips retrieved successfully"
                        )

                        .data(
                                healthTipService
                                        .getTipsByCondition(
                                                conditionTag
                                        )
                        )

                        .build()
        );
    }


    // ============================================================
    // GET TIP BY ID
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HealthTipResponse>>
    getHealthTip(
            @PathVariable Long id) {

        return ResponseEntity.ok(

                ApiResponse.<HealthTipResponse>builder()

                        .success(true)

                        .message(
                                "Health tip retrieved successfully"
                        )

                        .data(
                                healthTipService
                                        .getHealthTip(id)
                        )

                        .build()
        );
    }


    // ============================================================
    // DEACTIVATE TIP
    // ============================================================

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<HealthTipResponse>>
    deactivateHealthTip(
            @PathVariable Long id) {

        return ResponseEntity.ok(

                ApiResponse.<HealthTipResponse>builder()

                        .success(true)

                        .message(
                                "Health tip deactivated successfully"
                        )

                        .data(
                                healthTipService
                                        .deactivateHealthTip(id)
                        )

                        .build()
        );
    }


    // ============================================================
    // DELETE TIP
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>>
    deleteHealthTip(
            @PathVariable Long id) {

        healthTipService.deleteHealthTip(id);

        return ResponseEntity.ok(

                ApiResponse.<Void>builder()

                        .success(true)

                        .message(
                                "Health tip deleted successfully"
                        )

                        .build()
        );
    }
}