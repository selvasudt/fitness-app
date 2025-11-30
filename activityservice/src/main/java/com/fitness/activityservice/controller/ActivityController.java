package com.fitness.activityservice.controller;


import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody @Valid ActivityRequest activityRequest){
        ActivityResponse activityResponse = activityService.trackActivity(activityRequest);
        return ResponseEntity.ok(activityResponse);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@PathVariable("userId") String userId){
        List<ActivityResponse> activityResponse = activityService.getUserActivities(userId);
        return ResponseEntity.ok(activityResponse);
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable("activityId") String activityId){
        ActivityResponse activityResponse = activityService.getActivityById(activityId);
        return ResponseEntity.ok(activityResponse);
    }


}
