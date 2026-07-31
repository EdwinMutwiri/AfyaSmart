package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.dto.DashboardAnalyticsResponse;
import com.afyasmart.backend.entity.Account;
import com.afyasmart.backend.entity.Appointment;
import com.afyasmart.backend.entity.AppointmentStatus;
import com.afyasmart.backend.entity.Role;
import com.afyasmart.backend.repository.AccountRepository;
import com.afyasmart.backend.repository.AppointmentRepository;
import com.afyasmart.backend.service.DashboardAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardAnalyticsServiceImpl
        implements DashboardAnalyticsService {

    private final AccountRepository accountRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public DashboardAnalyticsResponse getAnalytics() {

        // ===============================
        // User Statistics
        // ===============================

        long totalUsers = accountRepository.count();

        long totalPatients =
                accountRepository.countByRole(Role.PATIENT);

        long totalDoctors =
                accountRepository.countByRole(Role.DOCTOR);

        long totalAdmins =
                accountRepository.countByRole(Role.ADMIN);

        // ===============================
        // Appointment Statistics
        // ===============================

        long totalAppointments =
                appointmentRepository.count();

        long pendingAppointments =
                appointmentRepository.countByStatus(
                        AppointmentStatus.PENDING);

        long confirmedAppointments =
                appointmentRepository.countByStatus(
                        AppointmentStatus.CONFIRMED);

        long completedAppointments =
                appointmentRepository.countByStatus(
                        AppointmentStatus.COMPLETED);

        long cancelledAppointments =
                appointmentRepository.countByStatus(
                        AppointmentStatus.CANCELLED);

        // ===============================
        // User Role Chart
        // ===============================

        Map<String, Long> userRoles = new HashMap<>();

        userRoles.put("Patients", totalPatients);
        userRoles.put("Doctors", totalDoctors);
        userRoles.put("Admins", totalAdmins);

        // ===============================
        // Appointment Status Chart
        // ===============================

        Map<String, Long> appointmentStatus = new HashMap<>();

        appointmentStatus.put("Pending", pendingAppointments);
        appointmentStatus.put("Confirmed", confirmedAppointments);
        appointmentStatus.put("Completed", completedAppointments);
        appointmentStatus.put("Cancelled", cancelledAppointments);

        // ===============================
        // Doctor Workload
        // ===============================

        Map<String, Long> doctorWorkload = new HashMap<>();

        List<Account> doctors =
                accountRepository.findByRole(Role.DOCTOR);

        List<Appointment> appointments =
                appointmentRepository.findAll();

        for (Account doctor : doctors) {

            String doctorName =
                    doctor.getFirstName() + " " + doctor.getLastName();

            long count = appointments.stream()
                    .filter(a -> doctorName.equals(a.getDoctorName()))
                    .count();

            doctorWorkload.put(doctorName, count);

        }

        // ===============================
        // Response
        // ===============================

        return DashboardAnalyticsResponse.builder()

                .totalUsers(totalUsers)
                .totalPatients(totalPatients)
                .totalDoctors(totalDoctors)
                .totalAdmins(totalAdmins)

                .totalAppointments(totalAppointments)

                .pendingAppointments(pendingAppointments)
                .confirmedAppointments(confirmedAppointments)
                .completedAppointments(completedAppointments)
                .cancelledAppointments(cancelledAppointments)

                .doctorWorkload(doctorWorkload)
                .appointmentStatus(appointmentStatus)
                .userRoles(userRoles)

                .build();

    }

}