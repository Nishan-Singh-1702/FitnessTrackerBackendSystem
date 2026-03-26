package com.Fitness.service;

import com.Fitness.exception.APIException;
import com.Fitness.exception.ResourceNotFoundException;
import com.Fitness.model.Activity;
import com.Fitness.model.User;
import com.Fitness.payload.ActivityDTO;
import com.Fitness.payload.ActivityResponse;
import com.Fitness.repository.ActivityRepository;
import com.Fitness.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ActivityDTO trackActivity(ActivityDTO activityDTO) {
        User userFromDb = userRepository.findById(activityDTO.getUserId())
                .orElseThrow(()->new ResourceNotFoundException("User","userId: ",activityDTO.getUserId()));
        Activity savedActivity = modelMapper.map(activityDTO, Activity.class);
        savedActivity.setUser(userFromDb);
        return modelMapper.map(activityRepository.save(savedActivity),ActivityDTO.class);
    }

    @Override
    public ActivityResponse getUsersActivity(Long userId) {
        List<Activity> activityFromDb = activityRepository.findByUserUserId(userId);
        if(activityFromDb.isEmpty()) throw new ResourceNotFoundException("Activities","userId",userId);
        List<ActivityDTO> activityDTOS = activityFromDb.stream()
                .map(activity->modelMapper.map(activity,ActivityDTO.class))
                .toList();
        ActivityResponse response = new ActivityResponse();
        response.setContent(activityDTOS);
        return response;
    }
}
