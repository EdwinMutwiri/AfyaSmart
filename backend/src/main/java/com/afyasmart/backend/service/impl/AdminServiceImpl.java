package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.assessment.repository.HealthAssessmentRepository;
import com.afyasmart.backend.dto.AdminDashboardResponse;
import com.afyasmart.backend.entity.Role;
import com.afyasmart.backend.repository.AccountRepository;
import com.afyasmart.backend.repository.AppointmentRepository;
import com.afyasmart.backend.repository.DoctorProfileRepository;
import com.afyasmart.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AccountRepository accountRepository;

    private final DoctorProfileRepository doctorRepository;

    private final AppointmentRepository appointmentRepository;

    private final HealthAssessmentRepository assessmentRepository;

    @Override
    public AdminDashboardResponse getDashboardStatistics() {

        long totalPatients = accountRepository.findAll()
                .stream()
                .filter(account -> account.getRole() == Role.PATIENT)
                .count();

        long totalDoctors = doctorRepository.count();

        long totalAppointments = appointmentRepository.count();

        long totalAssessments = assessmentRepository.count();

        return AdminDashboardResponse.builder()
                .totalPatients(totalPatients)
                .totalDoctors(totalDoctors)
                .totalAppointments(totalAppointments)
                .totalAssessments(totalAssessments)
                .build();
    }

}