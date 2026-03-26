package com.Fitness.repository;

import com.Fitness.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByUserUserId(Long userId);

    List<Recommendation> findByActivityActivityId(Long activityId);
}
