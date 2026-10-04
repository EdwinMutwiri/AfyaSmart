package com.afyasmart.backend.telemedicine.service.impl;

import com.afyasmart.backend.entity.Appointment;
import com.afyasmart.backend.entity.AppointmentStatus;
import com.afyasmart.backend.exception.ResourceNotFoundException;
import com.afyasmart.backend.repository.AppointmentRepository;
import com.afyasmart.backend.telemedicine.dto.TelemedicineRequest;
import com.afyasmart.backend.telemedicine.dto.TelemedicineResponse;
import com.afyasmart.backend.telemedicine.entity.TelemedicineConsultation;
import com.afyasmart.backend.telemedicine.repository.TelemedicineRepository;
import com.afyasmart.backend.telemedicine.service.TelemedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TelemedicineServiceImpl implements TelemedicineService {

    private final TelemedicineRepository telemedicineRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public TelemedicineResponse createConsultation(
            TelemedicineRequest request) {

        Appointment appointment = appointmentRepository
                .findById(request.getAppointmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Telemedicine consultation can only be created for a confirmed appointment");
        }

        if (telemedicineRepository
                .findByAppointmentId(appointment.getId())
                .isPresent()) {

            throw new IllegalStateException(
                    "A telemedicine consultation already exists for this appointment");
        }

        String meetingLink =
                "https://meet.afyasmart.local/" +
                        UUID.randomUUID();

        TelemedicineConsultation consultation =
                TelemedicineConsultation.builder()
                        .appointment(appointment)
                        .meetingLink(meetingLink)
                        .status("READY")
                        .build();

        return mapToResponse(
                telemedicineRepository.save(consultation));
    }

    @Override
    public TelemedicineResponse getConsultation(Long id) {

        return mapToResponse(
                telemedicineRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Telemedicine consultation not found")));
    }

    @Override
    public TelemedicineResponse getByAppointment(Long appointmentId) {

        return mapToResponse(
                telemedicineRepository.findByAppointmentId(appointmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Telemedicine consultation not found")));
    }

    @Override
    public List<TelemedicineResponse> getPatientConsultations(
            Long accountId) {

        return telemedicineRepository
                .findByAppointmentAccountId(accountId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<TelemedicineResponse> getDoctorConsultations(
            String doctorName) {

        return telemedicineRepository
                .findByAppointmentDoctorName(doctorName)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TelemedicineResponse startConsultation(Long id) {

        TelemedicineConsultation consultation =
                telemedicineRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Telemedicine consultation not found"));

        consultation.setStatus("IN_PROGRESS");
        consultation.setStartedAt(
                java.time.LocalDateTime.now());

        return mapToResponse(
                telemedicineRepository.save(consultation));
    }

    @Override
    public TelemedicineResponse completeConsultation(Long id) {

        TelemedicineConsultation consultation =
                telemedicineRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Telemedicine consultation not found"));

        consultation.setStatus("COMPLETED");
        consultation.setEndedAt(
                java.time.LocalDateTime.now());

        return mapToResponse(
                telemedicineRepository.save(consultation));
    }

    @Override
    public TelemedicineResponse cancelConsultation(Long id) {

        TelemedicineConsultation consultation =
                telemedicineRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Telemedicine consultation not found"));

        consultation.setStatus("CANCELLED");

        return mapToResponse(
                telemedicineRepository.save(consultation));
    }

    private TelemedicineResponse mapToResponse(
            TelemedicineConsultation consultation) {

        Appointment appointment = consultation.getAppointment();

        return TelemedicineResponse.builder()
                .id(consultation.getId())
                .appointmentId(appointment.getId())
                .patientName(
                        appointment.getAccount().getFirstName()
                                + " "
                                + appointment.getAccount().getLastName())
                .patientEmail(
                        appointment.getAccount().getEmail())
                .doctorName(appointment.getDoctorName())
                .specialization(appointment.getSpecialization())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .reason(appointment.getReason())
                .meetingLink(consultation.getMeetingLink())
                .status(consultation.getStatus())
                .createdAt(consultation.getCreatedAt())
                .startedAt(consultation.getStartedAt())
                .endedAt(consultation.getEndedAt())
                .build();
    }
}