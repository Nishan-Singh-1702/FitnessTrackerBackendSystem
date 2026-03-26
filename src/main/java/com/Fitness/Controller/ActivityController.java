package com.Fitness.Controller;

import com.Fitness.payload.ActivityDTO;
import com.Fitness.payload.ActivityResponse;
import com.Fitness.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @PostMapping("/admin/activity")
    public ResponseEntity<ActivityDTO> trackActivity(@Valid @RequestBody ActivityDTO activityDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.trackActivity(activityDTO));
    }

    @GetMapping("/public/activity/{userId}")
    public ResponseEntity<ActivityResponse> getUsersActivities(@PathVariable Long userId){
        return ResponseEntity.ok(activityService.getUsersActivity(userId));
    }
}
