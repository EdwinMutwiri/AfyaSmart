package com.afyasmart.backend.assessment.service.impl;

import com.afyasmart.backend.assessment.dto.HealthAssessmentRequest;
import com.afyasmart.backend.assessment.dto.HealthAssessmentResponse;
import com.afyasmart.backend.assessment.entity.HealthAssessment;
import com.afyasmart.backend.assessment.repository.HealthAssessmentRepository;
import com.afyasmart.backend.assessment.service.HealthAssessmentService;
import com.afyasmart.backend.entity.Account;
import com.afyasmart.backend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthAssessmentServiceImpl implements HealthAssessmentService {

    private final HealthAssessmentRepository repository;
    private final AccountRepository accountRepository;

    @Override
    public HealthAssessmentResponse analyze(HealthAssessmentRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        double bmi = calculateBMI(request.getWeight(), request.getHeight());

        int score = calculateHealthScore(request, bmi);

        String risk = determineRisk(score);

        String recommendation = generateRecommendation(request, bmi);

        HealthAssessment assessment = HealthAssessment.builder()
                .account(account)
                .age(request.getAge())
                .gender(request.getGender())
                .height(request.getHeight())
                .weight(request.getWeight())
                .systolic(request.getSystolic())
                .diastolic(request.getDiastolic())
                .bloodSugar(request.getBloodSugar())
                .exerciseDays(request.getExerciseDays())
                .smoker(request.getSmoker())
                .alcohol(request.getAlcohol())
                .bmi(bmi)
                .healthScore(score)
                .riskLevel(risk)
                .recommendation(recommendation)
                .build();

        repository.save(assessment);

        return HealthAssessmentResponse.builder()
                .bmi(bmi)
                .healthScore(score)
                .riskLevel(risk)
                .recommendation(recommendation)
                .assessmentDate(assessment.getAssessmentDate())
                .build();
    }

    @Override
    public HealthAssessmentResponse getLatestAssessment(Long accountId) {

        HealthAssessment assessment = repository
                .findTopByAccountIdOrderByAssessmentDateDesc(accountId)
                .orElseThrow(() -> new RuntimeException("No assessments found."));

        return HealthAssessmentResponse.builder()
                .bmi(assessment.getBmi())
                .healthScore(assessment.getHealthScore())
                .riskLevel(assessment.getRiskLevel())
                .recommendation(assessment.getRecommendation())
                .assessmentDate(assessment.getAssessmentDate())
                .build();

    }

    private double calculateBMI(double weight, double heightCm) {

        double height = heightCm / 100.0;

        return Math.round((weight / (height * height)) * 100.0) / 100.0;

    }

    private int calculateHealthScore(HealthAssessmentRequest request, double bmi) {

        int score = 100;

        if (bmi >= 25 && bmi < 30)
            score -= 10;

        if (bmi >= 30)
            score -= 20;

        if (request.getSystolic() >= 140 || request.getDiastolic() >= 90)
            score -= 20;

        if (Boolean.TRUE.equals(request.getSmoker()))
            score -= 15;

        if (Boolean.TRUE.equals(request.getAlcohol()))
            score -= 5;

        if (request.getExerciseDays() == 0)
            score -= 15;
        else if (request.getExerciseDays() <= 2)
            score -= 5;

        return Math.max(score, 0);

    }

    private String determineRisk(int score) {

        if (score >= 80)
            return "LOW";

        if (score >= 60)
            return "MODERATE";

        if (score >= 40)
            return "HIGH";

        return "CRITICAL";

    }

    private String generateRecommendation(HealthAssessmentRequest request,
                                          double bmi) {

        StringBuilder advice = new StringBuilder();

        if (bmi >= 25)
            advice.append("Maintain a healthy diet and exercise regularly. ");

        if (request.getSystolic() >= 140 || request.getDiastolic() >= 90)
            advice.append("Monitor blood pressure and reduce salt intake. ");

        if (Boolean.TRUE.equals(request.getSmoker()))
            advice.append("Consider quitting smoking. ");

        if (Boolean.TRUE.equals(request.getAlcohol()))
            advice.append("Reduce alcohol consumption. ");

        if (request.getExerciseDays() < 3)
            advice.append("Exercise at least 150 minutes per week.");

        if (advice.isEmpty())
            advice.append("Excellent health! Keep maintaining your lifestyle.");

        return advice.toString();

    }

    @Override
    public List<HealthAssessmentResponse> getAssessmentHistory(Long accountId) {

        return repository
                .findByAccountIdOrderByAssessmentDateDesc(accountId)
                .stream()
                .map(assessment -> HealthAssessmentResponse.builder()
                        .bmi(assessment.getBmi())
                        .healthScore(assessment.getHealthScore())
                        .riskLevel(assessment.getRiskLevel())
                        .recommendation(assessment.getRecommendation())
                        .assessmentDate(assessment.getAssessmentDate())
                        .build())
                .toList();

    }

}