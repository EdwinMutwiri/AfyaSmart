package com.afyasmart.backend.telemedicine.controller;

import com.afyasmart.backend.common.ApiResponse;
import com.afyasmart.backend.telemedicine.dto.TelemedicineRequest;
import com.afyasmart.backend.telemedicine.dto.TelemedicineResponse;
import com.afyasmart.backend.telemedicine.service.TelemedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/telemedicine")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TelemedicineController {

    private final TelemedicineService telemedicineService;

    @PostMapping
    public ResponseEntity<ApiResponse<TelemedicineResponse>>
    createConsultation(
            @RequestBody TelemedicineRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<TelemedicineResponse>builder()
                        .success(true)
                        .message("Telemedicine consultation created successfully")
                        .data(telemedicineService.createConsultation(request))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TelemedicineResponse>>
    getConsultation(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<TelemedicineResponse>builder()
                        .success(true)
                        .message("Telemedicine consultation retrieved successfully")
                        .data(telemedicineService.getConsultation(id))
                        .build()
        );
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<ApiResponse<TelemedicineResponse>>
    getByAppointment(@PathVariable Long appointmentId) {

        return ResponseEntity.ok(
                ApiResponse.<TelemedicineResponse>builder()
                        .success(true)
                        .message("Telemedicine consultation retrieved successfully")
                        .data(telemedicineService.getByAppointment(appointmentId))
                        .build()
        );
    }

    @GetMapping("/patient/{accountId}")
    public ResponseEntity<ApiResponse<List<TelemedicineResponse>>>
    getPatientConsultations(@PathVariable Long accountId) {

        return ResponseEntity.ok(
                ApiResponse.<List<TelemedicineResponse>>builder()
                        .success(true)
                        .message("Patient telemedicine consultations retrieved successfully")
                        .data(telemedicineService.getPatientConsultations(accountId))
                        .build()
        );
    }

    @GetMapping("/doctor/{doctorName}")
    public ResponseEntity<ApiResponse<List<TelemedicineResponse>>>
    getDoctorConsultations(@PathVariable String doctorName) {

        return ResponseEntity.ok(
                ApiResponse.<List<TelemedicineResponse>>builder()
                        .success(true)
                        .message("Doctor telemedicine consultations retrieved successfully")
                        .data(telemedicineService.getDoctorConsultations(doctorName))
                        .build()
        );
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<ApiResponse<TelemedicineResponse>>
    startConsultation(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<TelemedicineResponse>builder()
                        .success(true)
                        .message("Telemedicine consultation started")
                        .data(telemedicineService.startConsultation(id))
                        .build()
        );
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<TelemedicineResponse>>
    completeConsultation(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<TelemedicineResponse>builder()
                        .success(true)
                        .message("Telemedicine consultation completed")
                        .data(telemedicineService.completeConsultation(id))
                        .build()
        );
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<TelemedicineResponse>>
    cancelConsultation(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<TelemedicineResponse>builder()
                        .success(true)
                        .message("Telemedicine consultation cancelled")
                        .data(telemedicineService.cancelConsultation(id))
                        .build()
        );
    }
}