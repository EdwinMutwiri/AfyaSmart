package com.afyasmart.backend.service;

import com.afyasmart.backend.dto.AppointmentRequest;
import com.afyasmart.backend.dto.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest request);

    List<AppointmentResponse> getAppointments(Long accountId);

    void cancelAppointment(Long appointmentId);
}