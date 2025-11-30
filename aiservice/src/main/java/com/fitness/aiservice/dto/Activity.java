package com.fitness.aiservice.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;


@Data
@Builder
public class Activity {

    private String id;
    private String userId;
    private String type;
    private Double duration; // in minutes
    private Double distance; // in kilometers
    private Integer caloriesBurned;
    private LocalDateTime activityStartDate;
    private LocalDateTime activityEndDate;
    private Map<String, Object> additionalMetrics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
