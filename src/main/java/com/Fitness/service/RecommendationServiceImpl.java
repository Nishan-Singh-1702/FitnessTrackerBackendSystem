package com.Fitness.service;

import com.Fitness.exception.APIException;
import com.Fitness.exception.ResourceNotFoundException;
import com.Fitness.model.Activity;
import com.Fitness.model.Recommendation;
import com.Fitness.model.User;
import com.Fitness.payload.RecommendationDTO;
import com.Fitness.payload.RecommendationResponse;
import com.Fitness.repository.ActivityRepository;
import com.Fitness.repository.RecommendationRepository;
import com.Fitness.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService{
    @Autowired
    RecommendationRepository recommendationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public RecommendationDTO generateRecommendation(RecommendationDTO recommendationDTO) {
        User userFromDb = userRepository.findById(recommendationDTO.getUser().getUserId()).orElseThrow(()->new ResourceNotFoundException("User","userId",recommendationDTO.getUser().getUserId()));
        Activity activityFromDb = activityRepository.findById(recommendationDTO.getActivity().getActivityId()).orElseThrow(()->new ResourceNotFoundException("Activity","activityId",recommendationDTO.getActivity().getActivityId()));
        if(!activityFromDb.getUser().getUserId().equals(userFromDb.getUserId())) throw new APIException("Activity does not belong to this user");
        Recommendation recommendation = modelMapper.map(recommendationDTO,Recommendation.class);
        recommendation.setUser(userFromDb);
        recommendation.setActivity(activityFromDb);
        return modelMapper.map(recommendationRepository.save(recommendation),RecommendationDTO.class);
    }

    @Override
    public RecommendationResponse getUsersRecommendation(Long userId) {
        User userFromDb = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));

        List<Recommendation> recommendationFromDb = recommendationRepository.findByUserUserId(userId);
        if(recommendationFromDb.isEmpty()) throw new ResourceNotFoundException("Recommendation","userId",userId);
        List<RecommendationDTO> recommendationDTOS = recommendationFromDb.stream()
                .map(recommendation -> modelMapper.map(recommendation,RecommendationDTO.class))
                .toList();
        RecommendationResponse response = new RecommendationResponse();
        response.setContent(recommendationDTOS);
        return response;
    }

    @Override
    public RecommendationResponse getActivityRecommendation(Long activityId) {
        Activity activityFromDb = activityRepository.findById(activityId).orElseThrow(()->new ResourceNotFoundException("Activity","activityId",activityId));
        List<Recommendation> recommendationFromDb = recommendationRepository.findByActivityActivityId(activityId);
        if(recommendationFromDb.isEmpty()) throw new ResourceNotFoundException("Recommendation","activityId",activityId);
        List<RecommendationDTO> recommendationDTOS = recommendationFromDb.stream()
                .map(recommendation -> modelMapper.map(recommendation,RecommendationDTO.class))
                .toList();
        RecommendationResponse response = new RecommendationResponse();
        response.setContent(recommendationDTOS);
        return response;
    }
}
