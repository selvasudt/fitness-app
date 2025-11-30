package com.fitness.aiservice.dto.ai;

import lombok.Data;

import java.util.List;

@Data
public class AiRecommendationResponse {
    private Analysis analysis;
    private List<Improvement> improvements;
    private List<Suggestion> suggestions;
    private List<String> safety;
}