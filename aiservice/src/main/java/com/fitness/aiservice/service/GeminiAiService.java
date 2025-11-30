package com.fitness.aiservice.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiAiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String getAiResponse(String question) {
        Client client = Client.builder().apiKey(apiKey).build();
        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        question,
                        null);
        System.out.println(response.text());
        return response.text();
    }
}