package com.fitness.activityservice.dto;

import com.fitness.activityservice.model.ActivityType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;


@Data
@Builder
public class ActivityResponse {

    private String id;
    private String userId;
    private ActivityType type;
    private Double duration; // in minutes
    private Double distance; // in kilometers
    private Integer caloriesBurned;
    private LocalDateTime activityStartDate;
    private LocalDateTime activityEndDate;
    private Map<String, Object> additionalMetrics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
