package com.afyasmart.backend.repository;

import com.afyasmart.backend.entity.Appointment;
import com.afyasmart.backend.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByAccountId(Long accountId);

    List<Appointment> findByDoctorName(String doctorName);

    List<Appointment> findByDoctorNameOrderByAppointmentDateAscAppointmentTimeAsc(
            String doctorName
    );

    // NEW
    List<Appointment> findAllByOrderByAppointmentDateAscAppointmentTimeAsc();

    long countByStatus(AppointmentStatus status);

}