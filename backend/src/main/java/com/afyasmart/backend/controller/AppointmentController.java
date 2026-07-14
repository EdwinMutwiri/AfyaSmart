package com.afyasmart.backend.controller;

import com.afyasmart.backend.common.ApiResponse;
import com.afyasmart.backend.dto.AppointmentRequest;
import com.afyasmart.backend.dto.AppointmentResponse;
import com.afyasmart.backend.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            @RequestBody AppointmentRequest request) {

        AppointmentResponse response =
                appointmentService.createAppointment(request);

        return ResponseEntity.ok(
                ApiResponse.<AppointmentResponse>builder()
                        .success(true)
                        .message("Appointment booked successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointments(
            @PathVariable Long accountId) {

        return ResponseEntity.ok(
                ApiResponse.<List<AppointmentResponse>>builder()
                        .success(true)
                        .message("Appointments retrieved successfully")
                        .data(appointmentService.getAppointments(accountId))
                        .build()
        );
    }

    @GetMapping("/doctor/{doctorName}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getDoctorAppointments(
            @PathVariable String doctorName) {

        return ResponseEntity.ok(
                ApiResponse.<List<AppointmentResponse>>builder()
                        .success(true)
                        .message("Doctor appointments retrieved successfully")
                        .data(appointmentService.getDoctorAppointments(doctorName))
                        .build()
        );
    }

    @PutMapping("/confirm/{appointmentId}")
    public ResponseEntity<ApiResponse<String>> confirmAppointment(
            @PathVariable Long appointmentId) {

        appointmentService.confirmAppointment(appointmentId);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Appointment confirmed successfully")
                        .data("Confirmed")
                        .build()
        );
    }

    @PutMapping("/complete/{appointmentId}")
    public ResponseEntity<ApiResponse<String>> completeAppointment(
            @PathVariable Long appointmentId) {

        appointmentService.completeAppointment(appointmentId);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Appointment completed successfully")
                        .data("Completed")
                        .build()
        );
    }

    @PutMapping("/cancel/{appointmentId}")
    public ResponseEntity<ApiResponse<String>> cancelAppointment(
            @PathVariable Long appointmentId) {

        appointmentService.cancelAppointment(appointmentId);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Appointment cancelled successfully")
                        .data("Cancelled")
                        .build()
        );
    }
}