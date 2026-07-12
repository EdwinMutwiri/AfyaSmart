package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.dto.DoctorProfileRequest;
import com.afyasmart.backend.dto.DoctorProfileResponse;
import com.afyasmart.backend.entity.Account;
import com.afyasmart.backend.entity.DoctorProfile;
import com.afyasmart.backend.entity.Specialization;
import com.afyasmart.backend.exception.ResourceNotFoundException;
import com.afyasmart.backend.repository.AccountRepository;
import com.afyasmart.backend.repository.DoctorProfileRepository;
import com.afyasmart.backend.repository.SpecializationRepository;
import com.afyasmart.backend.service.DoctorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorProfileServiceImpl implements DoctorProfileService {

    private final DoctorProfileRepository doctorRepository;
    private final AccountRepository accountRepository;
    private final SpecializationRepository specializationRepository;

    @Override
    public DoctorProfileResponse createDoctor(DoctorProfileRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Specialization specialization = specializationRepository.findById(request.getSpecializationId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));

        DoctorProfile doctor = DoctorProfile.builder()
                .account(account)
                .specialization(specialization)
                .hospital(request.getHospital())
                .licenseNumber(request.getLicenseNumber())
                .yearsExperience(request.getYearsExperience())
                .consultationFee(request.getConsultationFee())
                .bio(request.getBio())
                .available(request.getAvailable())
                .build();

        DoctorProfile saved = doctorRepository.save(doctor);

        return map(saved);
    }

    @Override
    public List<DoctorProfileResponse> getAllDoctors() {

        return doctorRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<DoctorProfileResponse> getDoctorsBySpecialization(Long specializationId) {

        Specialization specialization = specializationRepository.findById(specializationId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));

        return doctorRepository.findBySpecialization(specialization)
                .stream()
                .map(this::map)
                .toList();
    }

    private DoctorProfileResponse map(DoctorProfile doctor) {

        return DoctorProfileResponse.builder()
                .id(doctor.getId())
                .accountId(doctor.getAccount().getId())
                .doctorName(
                        doctor.getAccount().getFirstName() + " " +
                                doctor.getAccount().getLastName()
                )
                .specialization(doctor.getSpecialization().getName())
                .hospital(doctor.getHospital())
                .licenseNumber(doctor.getLicenseNumber())
                .yearsExperience(doctor.getYearsExperience())
                .consultationFee(doctor.getConsultationFee())
                .bio(doctor.getBio())
                .available(doctor.getAvailable())
                .build();
    }
}