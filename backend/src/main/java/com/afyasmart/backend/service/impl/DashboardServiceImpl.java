package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.dto.DashboardStatsResponse;
import com.afyasmart.backend.entity.AppointmentStatus;
import com.afyasmart.backend.entity.Role;
import com.afyasmart.backend.repository.AccountRepository;
import com.afyasmart.backend.repository.AppointmentRepository;
import com.afyasmart.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final AccountRepository accountRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public DashboardStatsResponse getDashboardStats() {

        return DashboardStatsResponse.builder()

                .totalUsers(
                        accountRepository.count()
                )

                .totalPatients(
                        accountRepository.countByRole(Role.PATIENT)
                )

                .totalDoctors(
                        accountRepository.countByRole(Role.DOCTOR)
                )

                .totalAppointments(
                        appointmentRepository.count()
                )

                .pendingAppointments(
                        appointmentRepository.countByStatus(
                                AppointmentStatus.PENDING
                        )
                )

                .confirmedAppointments(
                        appointmentRepository.countByStatus(
                                AppointmentStatus.CONFIRMED
                        )
                )

                .completedAppointments(
                        appointmentRepository.countByStatus(
                                AppointmentStatus.COMPLETED
                        )
                )

                .cancelledAppointments(
                        appointmentRepository.countByStatus(
                                AppointmentStatus.CANCELLED
                        )
                )

                .build();
    }

}