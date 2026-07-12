package com.afyasmart.backend.repository;

import com.afyasmart.backend.entity.DoctorProfile;
import com.afyasmart.backend.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {

    Optional<DoctorProfile> findByAccountId(Long accountId);

    List<DoctorProfile> findBySpecialization(Specialization specialization);

    List<DoctorProfile> findByAvailableTrue();
}