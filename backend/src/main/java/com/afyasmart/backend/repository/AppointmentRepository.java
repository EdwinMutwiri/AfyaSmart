package com.afyasmart.backend.repository;

import com.afyasmart.backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByAccountId(Long accountId);

    List<Appointment> findByDoctorName(String doctorName);

}