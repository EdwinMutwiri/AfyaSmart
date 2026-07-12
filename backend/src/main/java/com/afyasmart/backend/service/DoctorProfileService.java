package com.afyasmart.backend.service;

import com.afyasmart.backend.dto.DoctorProfileRequest;
import com.afyasmart.backend.dto.DoctorProfileResponse;

import java.util.List;

public interface DoctorProfileService {

    DoctorProfileResponse createDoctor(DoctorProfileRequest request);

    List<DoctorProfileResponse> getAllDoctors();

    List<DoctorProfileResponse> getDoctorsBySpecialization(Long specializationId);

}