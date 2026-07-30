package com.afyasmart.backend.service;

import com.afyasmart.backend.dto.AppointmentRequest;
import com.afyasmart.backend.dto.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest request);

    List<AppointmentResponse> getAppointments(Long accountId);

    List<AppointmentResponse> getDoctorAppointments(String doctorName);

    List<AppointmentResponse> getAllAppointments();

    void cancelAppointment(Long appointmentId);

    AppointmentResponse confirmAppointment(Long appointmentId);

    AppointmentResponse completeAppointment(Long appointmentId);

}