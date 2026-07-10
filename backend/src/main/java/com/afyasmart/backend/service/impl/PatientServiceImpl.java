package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.dto.PatientDTO;
import com.afyasmart.backend.entity.Patient;
import com.afyasmart.backend.repository.PatientRepository;
import com.afyasmart.backend.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public Patient createPatient(PatientDTO dto) {

        if (patientRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("A patient with this email already exists.");
        }

        Patient patient = Patient.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .age(dto.getAge())
                .gender(dto.getGender())
                .bloodGroup(dto.getBloodGroup())
                .height(dto.getHeight())
                .weight(dto.getWeight())
                .address(dto.getAddress())
                .emergencyContact(dto.getEmergencyContact())
                .emergencyPhone(dto.getEmergencyPhone())
                .build();

        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Patient not found"));
    }

    @Override
    public Patient updatePatient(Long id, PatientDTO dto) {

        Patient patient = getPatientById(id);

        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setAge(dto.getAge());
        patient.setGender(dto.getGender());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setHeight(dto.getHeight());
        patient.setWeight(dto.getWeight());
        patient.setAddress(dto.getAddress());
        patient.setEmergencyContact(dto.getEmergencyContact());
        patient.setEmergencyPhone(dto.getEmergencyPhone());

        return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(Long id) {

        Patient patient = getPatientById(id);

        patientRepository.delete(patient);
    }
}