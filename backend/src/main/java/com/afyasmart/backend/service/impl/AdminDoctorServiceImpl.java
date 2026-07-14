package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.dto.CreateDoctorRequest;
import com.afyasmart.backend.dto.DoctorProfileResponse;
import com.afyasmart.backend.entity.Account;
import com.afyasmart.backend.entity.DoctorProfile;
import com.afyasmart.backend.entity.Role;
import com.afyasmart.backend.entity.Specialization;
import com.afyasmart.backend.exception.DuplicateResourceException;
import com.afyasmart.backend.exception.ResourceNotFoundException;
import com.afyasmart.backend.repository.AccountRepository;
import com.afyasmart.backend.repository.DoctorProfileRepository;
import com.afyasmart.backend.repository.SpecializationRepository;
import com.afyasmart.backend.service.AdminDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminDoctorServiceImpl implements AdminDoctorService {

    private final AccountRepository accountRepository;
    private final DoctorProfileRepository doctorRepository;
    private final SpecializationRepository specializationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public DoctorProfileResponse createDoctor(CreateDoctorRequest request) {

        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        Specialization specialization = specializationRepository
                .findById(request.getSpecializationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Specialization not found"));

        Account account = Account.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.DOCTOR)
                .enabled(true)
                .build();

        Account savedAccount = accountRepository.save(account);

        DoctorProfile doctor = DoctorProfile.builder()
                .account(savedAccount)
                .specialization(specialization)
                .hospital(request.getHospital())
                .licenseNumber(request.getLicenseNumber())
                .yearsExperience(request.getYearsExperience())
                .consultationFee(request.getConsultationFee())
                .bio(request.getBio())
                .available(true)
                .build();

        DoctorProfile savedDoctor = doctorRepository.save(doctor);

        return DoctorProfileResponse.builder()
                .id(savedDoctor.getId())
                .accountId(savedAccount.getId())
                .doctorName(
                        savedAccount.getFirstName() + " " +
                                savedAccount.getLastName()
                )
                .specialization(savedDoctor.getSpecialization().getName())
                .hospital(savedDoctor.getHospital())
                .licenseNumber(savedDoctor.getLicenseNumber())
                .yearsExperience(savedDoctor.getYearsExperience())
                .consultationFee(savedDoctor.getConsultationFee())
                .bio(savedDoctor.getBio())
                .available(savedDoctor.getAvailable())
                .build();
    }
}