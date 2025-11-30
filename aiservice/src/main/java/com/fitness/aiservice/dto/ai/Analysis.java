package com.fitness.aiservice.dto.ai;

import lombok.Data;

@Data
public class Analysis {
    private String overall;
    private String pace;
    private String heartRate;
    private String caloriesBurned;
}