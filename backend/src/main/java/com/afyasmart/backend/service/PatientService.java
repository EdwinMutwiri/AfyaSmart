package com.afyasmart.backend.service;

import com.afyasmart.backend.dto.PatientDTO;
import com.afyasmart.backend.entity.Patient;

import java.util.List;

public interface PatientService {

    Patient createPatient(PatientDTO patientDTO);

    List<Patient> getAllPatients();

    Patient getPatientById(Long id);

    Patient updatePatient(Long id, PatientDTO patientDTO);

    void deletePatient(Long id);
}