package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.dto.AppointmentRequest;
import com.afyasmart.backend.dto.AppointmentResponse;
import com.afyasmart.backend.entity.Account;
import com.afyasmart.backend.entity.Appointment;
import com.afyasmart.backend.entity.AppointmentStatus;
import com.afyasmart.backend.exception.ResourceNotFoundException;
import com.afyasmart.backend.repository.AccountRepository;
import com.afyasmart.backend.repository.AppointmentRepository;
import com.afyasmart.backend.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AccountRepository accountRepository;

    @Override
    public AppointmentResponse createAppointment(AppointmentRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Appointment appointment = Appointment.builder()
                .account(account)
                .doctorName(request.getDoctorName())
                .specialization(request.getSpecialization())
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .reason(request.getReason())
                .status(AppointmentStatus.PENDING)
                .build();

        Appointment saved = appointmentRepository.save(appointment);

        return mapToResponse(saved);
    }

    @Override
    public List<AppointmentResponse> getAppointments(Long accountId) {

        return appointmentRepository.findByAccountId(accountId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void cancelAppointment(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELLED);

        appointmentRepository.save(appointment);
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .doctorName(appointment.getDoctorName())
                .specialization(appointment.getSpecialization())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .reason(appointment.getReason())
                .status(appointment.getStatus())
                .build();
    }
}