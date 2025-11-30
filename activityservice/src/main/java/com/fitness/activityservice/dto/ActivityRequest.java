package com.fitness.activityservice.dto;

import com.fitness.activityservice.model.ActivityType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.Map;


@Data
public class ActivityRequest {

    @NotBlank(message = "User ID cannot be blank")
    private String userId;
    @NotNull(message = "Activity type cannot be null")
    private ActivityType type;
    @NotNull(message = "Duration cannot be null")
    @Min(value = 0, message = "Duration must be a positive value")
    private Double duration; // in minutes
    @Min(value = 0, message = "Distance must be a positive value")
    private Double distance; // in kilometers
    @Min(value = 0, message = "Calories burned must be a positive value")
    private Integer caloriesBurned;
    @NotNull(message = "Activity start date cannot be null")
    private LocalDateTime activityStartDate;
    @NotNull(message = "Activity end date cannot be null")
    private LocalDateTime activityEndDate;
    private Map<String, Object> additionalMetrics;

}
