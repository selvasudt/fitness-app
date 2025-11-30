package com.fitness.aiservice.service;


import com.fitness.aiservice.dto.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    ActivityAiService activityAiService;

    public List<Recommendation> getUserRecommendation(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public Recommendation getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId).orElseThrow(() -> new RuntimeException("There is no recommendation for this activity"));
    }

    public void generateAndSaveRecommendation(Activity activity) {
      Recommendation recommendation =  activityAiService.generateRecommendation(activity);
      recommendationRepository.save(recommendation);
    }
}
