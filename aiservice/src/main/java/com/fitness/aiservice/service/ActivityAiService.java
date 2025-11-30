package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.constants.Constants;
import com.fitness.aiservice.dto.Activity;
import com.fitness.aiservice.dto.ai.AiRecommendationResponse;
import com.fitness.aiservice.dto.ai.Analysis;
import com.fitness.aiservice.dto.ai.Improvement;
import com.fitness.aiservice.model.Recommendation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ActivityAiService {

    @Autowired
    private GeminiAiService geminiAiService;

    public Recommendation generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiAiService.getAiResponse(prompt);
        log.info("RESPONSE FROM AI: {} ", aiResponse);
        return processAiResponse(activity, aiResponse);
    }

    private Recommendation processAiResponse(Activity activity, String aiResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = aiResponse
                    .replaceAll("```json\\n","")
                    .replaceAll("\\n```", "")
                    .trim();
            
            AiRecommendationResponse response = mapper.readValue(jsonContent, AiRecommendationResponse.class);

            String fullAnalysis = Optional.ofNullable(response.getAnalysis())
                    .map(this::formatAnalysis)
                    .orElse("No detailed analysis available.");

            List<String> improvements = Optional.ofNullable(response.getImprovements())
                    .map(list -> list.stream().map(i -> String.format("%s: %s", i.getArea(), i.getRecommendation())).collect(Collectors.toList()))
                    .orElse(Collections.singletonList("No specific improvements provided."));

            List<String> suggestions = Optional.ofNullable(response.getSuggestions())
                    .map(list -> list.stream().map(s -> String.format("%s: %s", s.getWorkout(), s.getDescription())).collect(Collectors.toList()))
                    .orElse(Collections.singletonList("No specific suggestions provided."));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions.isEmpty() ? Collections.singletonList("No specific suggestions provided") : suggestions)
                    .safety(response.getSafety().isEmpty() ? Collections.singletonList("Follow general safety guidelines") : response.getSafety())
                    .createdAt(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return createDefaultRecommendation(activity);
        }
    }

    private String formatAnalysis(Analysis analysis) {
        StringBuilder sb = new StringBuilder();
        Optional.ofNullable(analysis.getOverall()).ifPresent(v -> sb.append("Overall: ").append(v).append("\n\n"));
        Optional.ofNullable(analysis.getPace()).ifPresent(v -> sb.append("Pace: ").append(v).append("\n\n"));
        Optional.ofNullable(analysis.getHeartRate()).ifPresent(v -> sb.append("Heart Rate: ").append(v).append("\n\n"));
        Optional.ofNullable(analysis.getCaloriesBurned()).ifPresent(v -> sb.append("Calories: ").append(v).append("\n\n"));
        return sb.toString().trim();
    }

    private List<String> formatImprovements(List<Improvement> improvements) {
        if (improvements == null || improvements.isEmpty()) return Collections.singletonList("No specific improvements provided.");
        return improvements.stream().map(i -> String.format("%s: %s", i.getArea(), i.getRecommendation())).collect(Collectors.toList());
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .recommendation("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue with your current routine"))
                .suggestions(Collections.singletonList("Consider consulting a fitness professional for personalized advice."))
                .safety(List.of("Always warm up before exercise.", "Stay hydrated.", "Listen to your body and avoid overexertion."))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private String createPromptForActivity(Activity activity) {
        return String.format(Constants.aiPromptTemplate,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }
}