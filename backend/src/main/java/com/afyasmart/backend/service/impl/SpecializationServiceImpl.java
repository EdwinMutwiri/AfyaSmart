package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.entity.Specialization;
import com.afyasmart.backend.repository.SpecializationRepository;
import com.afyasmart.backend.service.SpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository specializationRepository;

    @Override
    public List<Specialization> getAllSpecializations() {
        return specializationRepository.findAll();
    }
}