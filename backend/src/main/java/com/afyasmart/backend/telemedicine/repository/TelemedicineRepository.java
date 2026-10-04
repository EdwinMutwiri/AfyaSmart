package com.afyasmart.backend.telemedicine.repository;

import com.afyasmart.backend.telemedicine.entity.TelemedicineConsultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TelemedicineRepository
        extends JpaRepository<TelemedicineConsultation, Long> {

    Optional<TelemedicineConsultation> findByAppointmentId(Long appointmentId);

    List<TelemedicineConsultation> findByAppointmentAccountId(Long accountId);

    List<TelemedicineConsultation> findByAppointmentDoctorName(String doctorName);
}