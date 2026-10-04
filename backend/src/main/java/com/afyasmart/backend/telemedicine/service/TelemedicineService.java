package com.afyasmart.backend.telemedicine.service;

import com.afyasmart.backend.telemedicine.dto.TelemedicineRequest;
import com.afyasmart.backend.telemedicine.dto.TelemedicineResponse;

import java.util.List;

public interface TelemedicineService {

    TelemedicineResponse createConsultation(TelemedicineRequest request);

    TelemedicineResponse getConsultation(Long id);

    TelemedicineResponse getByAppointment(Long appointmentId);

    List<TelemedicineResponse> getPatientConsultations(Long accountId);

    List<TelemedicineResponse> getDoctorConsultations(String doctorName);

    TelemedicineResponse startConsultation(Long id);

    TelemedicineResponse completeConsultation(Long id);

    TelemedicineResponse cancelConsultation(Long id);
}