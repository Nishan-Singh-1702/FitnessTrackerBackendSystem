package com.Fitness.service;

import com.Fitness.payload.RecommendationDTO;
import com.Fitness.payload.RecommendationResponse;

public interface RecommendationService {
    RecommendationDTO generateRecommendation(RecommendationDTO recommendationDTO);
    RecommendationResponse getUsersRecommendation(Long userId);
    RecommendationResponse getActivityRecommendation(Long activityId);

}
