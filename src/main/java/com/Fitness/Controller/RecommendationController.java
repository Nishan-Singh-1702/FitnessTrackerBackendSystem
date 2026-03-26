package com.Fitness.Controller;

import com.Fitness.payload.RecommendationDTO;
import com.Fitness.payload.RecommendationResponse;
import com.Fitness.service.RecommendationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RecommendationController {

    @Autowired
    RecommendationService recommendationService;

    @PostMapping("/admin/recommendation")
    public ResponseEntity<RecommendationDTO> generateRecommendation(@Valid @RequestBody RecommendationDTO recommendationDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(recommendationService.generateRecommendation(recommendationDTO));
    }

    @GetMapping("/public/recommendation/user/{userId}")
    public ResponseEntity<RecommendationResponse> getUsersRecommendations(@PathVariable Long userId){
        return ResponseEntity.ok().body(recommendationService.getUsersRecommendation(userId));
    }

    @GetMapping("/public/recommendation/activity/{activityId}")
    public ResponseEntity<RecommendationResponse> getActivityRecommendations(@PathVariable Long activityId){
        return ResponseEntity.ok().body(recommendationService.getActivityRecommendation(activityId));
    }

}
