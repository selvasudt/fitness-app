package com.fitness.activityservice.service;


import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Service
@Slf4j
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserValidationService userValidationService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest activityRequest, @RequestHeader("X-User-ID") String userId ) {

        if (userId != null){
            activityRequest.setUserId(userId);
        }
                
        Boolean isValidUser = userValidationService.validateUser(activityRequest.getUserId());
        if (!isValidUser)
            throw new RuntimeException("Invalid User: " + activityRequest.getUserId());

        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .distance(activityRequest.getDistance())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .activityStartDate(activityRequest.getActivityStartDate())
                .activityEndDate(activityRequest.getActivityEndDate())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();
        Activity savedActivity = activityRepository.save(activity);

        // Publish to AI service for the recommendations
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
        } catch (Exception e) {
             log.error("Error while sending message to RabbitMQ: {}", e.getMessage());
        }

        return mapToResponse(savedActivity);

    }

    private ActivityResponse mapToResponse(Activity activity) {
        return ActivityResponse.builder()
                .id(activity.getId())
                .userId(activity.getUserId())
                .type(activity.getType())
                .duration(activity.getDuration())
                .distance(activity.getDistance())
                .caloriesBurned(activity.getCaloriesBurned())
                .activityStartDate(activity.getActivityStartDate())
                .activityEndDate(activity.getActivityEndDate())
                .additionalMetrics(activity.getAdditionalMetrics())
                .createdAt(activity.getCreatedAt())
                .updatedAt(activity.getUpdatedAt())
                .build();

    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
        return activities.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ActivityResponse getActivityById(String activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found: " + activityId));
        return mapToResponse(activity);
    }
}
