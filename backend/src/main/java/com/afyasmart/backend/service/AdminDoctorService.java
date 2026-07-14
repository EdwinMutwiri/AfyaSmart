package com.afyasmart.backend.service;

import com.afyasmart.backend.dto.CreateDoctorRequest;
import com.afyasmart.backend.dto.DoctorProfileResponse;

public interface AdminDoctorService {

    DoctorProfileResponse createDoctor(CreateDoctorRequest request);

}