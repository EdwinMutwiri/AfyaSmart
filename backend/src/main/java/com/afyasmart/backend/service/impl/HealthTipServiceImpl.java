package com.afyasmart.backend.service.impl;

import com.afyasmart.backend.ai.HealthTip;
import com.afyasmart.backend.assessment.entity.HealthAssessment;
import com.afyasmart.backend.assessment.repository.HealthAssessmentRepository;
import com.afyasmart.backend.dto.HealthTipRecommendationRequest;
import com.afyasmart.backend.dto.HealthTipRequest;
import com.afyasmart.backend.dto.HealthTipResponse;
import com.afyasmart.backend.repository.HealthTipRepository;
import com.afyasmart.backend.service.HealthTipService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class HealthTipServiceImpl implements HealthTipService {

    private final HealthTipRepository healthTipRepository;

    /*
     * Repository used to retrieve the patient's
     * latest health assessment.
     */
    private final HealthAssessmentRepository healthAssessmentRepository;


    // ============================================================
    // CREATE HEALTH TIP
    // ============================================================

    @Override
    public HealthTipResponse createHealthTip(
            HealthTipRequest request) {

        if (request.getTitle() == null ||
                request.getTitle().isBlank()) {

            throw new IllegalArgumentException(
                    "Health tip title is required."
            );
        }

        if (request.getContent() == null ||
                request.getContent().isBlank()) {

            throw new IllegalArgumentException(
                    "Health tip content is required."
            );
        }

        if (request.getCategory() == null ||
                request.getCategory().isBlank()) {

            throw new IllegalArgumentException(
                    "Health tip category is required."
            );
        }

        HealthTip healthTip = new HealthTip();

        healthTip.setTitle(
                request.getTitle().trim()
        );

        healthTip.setContent(
                request.getContent().trim()
        );

        healthTip.setCategory(
                request.getCategory()
                        .trim()
                        .toUpperCase()
        );

        if (request.getConditionTag() != null) {

            healthTip.setConditionTag(
                    request.getConditionTag().trim()
            );
        }

        if (request.getActive() != null) {

            healthTip.setActive(
                    request.getActive()
            );

        } else {

            healthTip.setActive(true);
        }

        HealthTip saved =
                healthTipRepository.save(healthTip);

        return mapToResponse(saved);
    }


    // ============================================================
    // GET ALL ACTIVE HEALTH TIPS
    // ============================================================

    @Override
    public List<HealthTipResponse> getAllActiveTips() {

        return healthTipRepository
                .findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ============================================================
    // DAILY HEALTH TIP
    // ============================================================

    @Override
    public HealthTipResponse getDailyTip() {

        List<HealthTip> tips =
                healthTipRepository.findByActiveTrue();

        if (tips.isEmpty()) {

            throw new RuntimeException(
                    "No active health tips available."
            );
        }

        /*
         * Sort first so the same database contents
         * always produce the same daily result.
         */
        tips.sort(
                Comparator.comparing(
                        HealthTip::getId
                )
        );

        /*
         * Use the current day to select a tip.
         *
         * This gives us a simple deterministic
         * daily health tip without needing another
         * database table.
         */
        long dayNumber =
                LocalDate.now().toEpochDay();

        int index =
                (int) Math.floorMod(
                        dayNumber,
                        tips.size()
                );

        return mapToResponse(
                tips.get(index)
        );
    }


    // ============================================================
    // MANUAL AI RECOMMENDATION
    // ============================================================

    @Override
    public HealthTipResponse getRecommendedTip(
            HealthTipRecommendationRequest request) {

        List<HealthTip> tips =
                healthTipRepository.findByActiveTrue();

        if (tips.isEmpty()) {

            throw new RuntimeException(
                    "No active health tips available."
            );
        }

        String riskLevel =
                normalize(request.getRiskLevel());

        String category =
                normalize(request.getCategory());

        String conditionTag =
                normalize(request.getConditionTag());

        Double bmi =
                request.getBmi();

        HealthTip bestTip = null;

        int bestScore = Integer.MIN_VALUE;

        for (HealthTip tip : tips) {

            int score = 0;

            String tipCategory =
                    normalize(tip.getCategory());

            String tipCondition =
                    normalize(tip.getConditionTag());


            // ----------------------------------------------------
            // CATEGORY MATCH
            // ----------------------------------------------------

            if (!category.isBlank()
                    && category.equals(tipCategory)) {

                score += 40;
            }


            // ----------------------------------------------------
            // CONDITION MATCH
            // ----------------------------------------------------

            if (!conditionTag.isBlank()
                    && conditionTag.equals(tipCondition)) {

                score += 50;
            }


            // ----------------------------------------------------
            // RISK LEVEL
            // ----------------------------------------------------

            if ("HIGH".equals(riskLevel)) {

                if ("PREVENTION".equals(tipCategory)
                        || "MEDICATION".equals(tipCategory)
                        || "HIGH_RISK".equals(tipCategory)) {

                    score += 35;
                }

            } else if ("MODERATE".equals(riskLevel)
                    || "MEDIUM".equals(riskLevel)) {

                if ("NUTRITION".equals(tipCategory)
                        || "EXERCISE".equals(tipCategory)
                        || "PREVENTION".equals(tipCategory)) {

                    score += 25;
                }

            } else if ("LOW".equals(riskLevel)) {

                if ("PREVENTION".equals(tipCategory)
                        || "EXERCISE".equals(tipCategory)
                        || "HYDRATION".equals(tipCategory)) {

                    score += 15;
                }
            }


            // ----------------------------------------------------
            // BMI
            // ----------------------------------------------------

            if (bmi != null) {

                if (bmi < 18.5) {

                    if ("UNDERWEIGHT".equals(tipCondition)
                            || "NUTRITION".equals(tipCategory)) {

                        score += 35;
                    }

                } else if (bmi < 25) {

                    if ("NORMAL_BMI".equals(tipCondition)
                            || "PREVENTION".equals(tipCategory)) {

                        score += 20;
                    }

                } else {

                    if ("OVERWEIGHT".equals(tipCondition)
                            || "EXERCISE".equals(tipCategory)
                            || "NUTRITION".equals(tipCategory)) {

                        score += 35;
                    }
                }
            }


            // ----------------------------------------------------
            // GENERAL TIP
            // ----------------------------------------------------

            if ("GENERAL".equals(tipCondition)) {

                score += 5;
            }


            // ----------------------------------------------------
            // SELECT BEST TIP
            // ----------------------------------------------------

            if (bestTip == null
                    || score > bestScore) {

                bestTip = tip;
                bestScore = score;
            }
        }

        return mapToResponse(bestTip);
    }


    // ============================================================
    // AUTOMATIC PATIENT RECOMMENDATION
    // ============================================================

    @Override
    public HealthTipResponse getRecommendedTipForPatient(
            Long accountId) {

        /*
         * Retrieve the patient's most recent assessment.
         *
         * The existing Assessment module already provides
         * exactly what we need through the repository.
         */
        HealthAssessment assessment =
                healthAssessmentRepository
                        .findTopByAccountIdOrderByAssessmentDateDesc(
                                accountId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No health assessment found for this patient."
                                )
                        );


        /*
         * Extract BMI from the actual assessment.
         */
        Double bmi =
                assessment.getBmi();


        /*
         * Extract the risk level calculated by
         * the existing Assessment module.
         */
        String riskLevel =
                assessment.getRiskLevel();


        /*
         * Determine a useful health condition
         * from the patient's BMI.
         */
        String conditionTag =
                determineConditionFromBMI(bmi);


        /*
         * Determine which health-tip category is
         * most appropriate for the patient's assessment.
         */
        String category =
                determineCategoryFromAssessment(
                        assessment
                );


        /*
         * Build the same request object that our
         * existing recommendation engine already uses.
         *
         * This means we are reusing our existing AI
         * recommendation logic instead of duplicating it.
         */
        HealthTipRecommendationRequest recommendationRequest =
                new HealthTipRecommendationRequest();

        recommendationRequest.setBmi(bmi);

        recommendationRequest.setRiskLevel(
                riskLevel
        );

        recommendationRequest.setCategory(
                category
        );

        recommendationRequest.setConditionTag(
                conditionTag
        );


        /*
         * Send the automatically prepared information
         * through our existing recommendation engine.
         */
        return getRecommendedTip(
                recommendationRequest
        );
    }


    // ============================================================
    // DETERMINE CONDITION FROM BMI
    // ============================================================

    private String determineConditionFromBMI(
            Double bmi) {

        if (bmi == null) {

            return "GENERAL";
        }

        if (bmi < 18.5) {

            return "UNDERWEIGHT";
        }

        if (bmi < 25) {

            return "NORMAL_BMI";
        }

        return "OVERWEIGHT";
    }


    // ============================================================
    // DETERMINE CATEGORY FROM ASSESSMENT
    // ============================================================

    private String determineCategoryFromAssessment(
            HealthAssessment assessment) {

        /*
         * The Assessment module already creates a textual
         * recommendation based on the patient's results.
         *
         * We use that information to select the most
         * relevant health-tip category.
         */
        String recommendation =
                assessment.getRecommendation();

        if (recommendation == null) {

            recommendation = "";
        }

        recommendation =
                recommendation.toLowerCase(
                        Locale.ROOT
                );


        // --------------------------------------------------------
        // DIET / NUTRITION
        // --------------------------------------------------------

        if (recommendation.contains("diet")
                || recommendation.contains("nutrition")
                || recommendation.contains("healthy food")) {

            return "NUTRITION";
        }


        // --------------------------------------------------------
        // EXERCISE
        // --------------------------------------------------------

        if (recommendation.contains("exercise")
                || recommendation.contains("physical activity")) {

            return "EXERCISE";
        }


        // --------------------------------------------------------
        // SMOKING / ALCOHOL / BLOOD PRESSURE
        // --------------------------------------------------------

        if (recommendation.contains("smoking")
                || recommendation.contains("smoke")
                || recommendation.contains("alcohol")
                || recommendation.contains("blood pressure")
                || recommendation.contains("salt")) {

            return "PREVENTION";
        }


        // --------------------------------------------------------
        // BMI FALLBACK
        // --------------------------------------------------------

        Double bmi =
                assessment.getBmi();

        if (bmi != null && bmi >= 25) {

            return "NUTRITION";
        }


        /*
         * Safe general fallback when no specific
         * recommendation can be identified.
         */
        return "GENERAL";
    }


    // ============================================================
    // GET TIPS BY CATEGORY
    // ============================================================

    @Override
    public List<HealthTipResponse> getTipsByCategory(
            String category) {

        return healthTipRepository
                .findByCategoryAndActiveTrue(
                        category
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ============================================================
    // GET TIPS BY CONDITION
    // ============================================================

    @Override
    public List<HealthTipResponse> getTipsByCondition(
            String conditionTag) {

        return healthTipRepository
                .findByConditionTagAndActiveTrue(
                        conditionTag
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ============================================================
    // GET HEALTH TIP BY ID
    // ============================================================

    @Override
    public HealthTipResponse getHealthTip(
            Long id) {

        HealthTip tip =
                healthTipRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Health tip not found."
                                )
                        );

        return mapToResponse(tip);
    }


    // ============================================================
    // DEACTIVATE HEALTH TIP
    // ============================================================

    @Override
    public HealthTipResponse deactivateHealthTip(
            Long id) {

        HealthTip tip =
                healthTipRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Health tip not found."
                                )
                        );

        tip.setActive(false);

        HealthTip updated =
                healthTipRepository.save(tip);

        return mapToResponse(updated);
    }


    // ============================================================
    // DELETE HEALTH TIP
    // ============================================================

    @Override
    public void deleteHealthTip(
            Long id) {

        if (!healthTipRepository.existsById(id)) {

            throw new RuntimeException(
                    "Health tip not found."
            );
        }

        healthTipRepository.deleteById(id);
    }


    // ============================================================
    // MAP ENTITY TO RESPONSE
    // ============================================================

    private HealthTipResponse mapToResponse(
            HealthTip tip) {

        return HealthTipResponse.builder()
                .id(tip.getId())
                .title(tip.getTitle())
                .content(tip.getContent())
                .category(tip.getCategory())
                .conditionTag(tip.getConditionTag())
                .active(tip.getActive())
                .createdAt(tip.getCreatedAt())
                .build();
    }


    // ============================================================
    // NORMALIZE TEXT
    // ============================================================

    private String normalize(
            String value) {

        if (value == null) {

            return "";
        }

        return value
                .trim()
                .toUpperCase();
    }
}