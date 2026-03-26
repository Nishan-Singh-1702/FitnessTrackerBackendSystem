package com.Fitness.service;

import com.Fitness.payload.ActivityDTO;
import com.Fitness.payload.ActivityResponse;

public interface ActivityService {
    ActivityDTO trackActivity(ActivityDTO activityDTO);
    ActivityResponse getUsersActivity(Long userId);
}
