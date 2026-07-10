package com.afyasmart.backend.controller;

import com.afyasmart.backend.dto.PatientDTO;
import com.afyasmart.backend.entity.Patient;
import com.afyasmart.backend.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public Patient createPatient(@Valid @RequestBody PatientDTO dto) {
        return patientService.createPatient(dto);
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(
            @PathVariable Long id,
            @RequestBody PatientDTO dto) {

        return patientService.updatePatient(id, dto);
    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable Long id) {

        patientService.deletePatient(id);

        return "Patient deleted successfully.";
    }
}