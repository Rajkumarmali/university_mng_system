package com.example.UniversityManagementSystem.services.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Cloud-based AI Service powered by Groq's ultra-fast LPU inference engine.
 * Delivers responses in ~300-500ms with zero local CPU/RAM load.
 */
@Service
public class GroqCloudService implements AIService {

    private static final Logger logger = LoggerFactory.getLogger(GroqCloudService.class);

    private final WebClient webClient;
    private final String apiKey;
    private final String model;

    public GroqCloudService(
            @Value("${groq.base-url:https://api.groq.com/openai/v1}") String baseUrl,
            @Value("${groq.api-key:}") String apiKey,
            @Value("${groq.model:openai/gpt-oss-20b}") String model) {
        this.apiKey = apiKey != null ? apiKey.trim() : "";
        this.model = (model != null && !model.isBlank()) ? model.trim() : "openai/gpt-oss-20b";
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public boolean isConfigured() {
        return !apiKey.isEmpty() && !apiKey.equalsIgnoreCase("YOUR_GROQ_API_KEY");
    }

    @Override
    public String generateResponse(String prompt) {
        if (!isConfigured()) {
            return "Groq Cloud API is enabled, but the API key is missing.\n\n" +
                    "To activate instant cloud responses:\n" +
                    "1. Get your free API key at https://console.groq.com/keys\n" +
                    "2. Add it to application.properties:\n" +
                    "   groq.api-key=gsk_your_key_here\n" +
                    "3. Restart the backend.";
        }

        try {
            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content", "You are an intelligent university management system AI assistant. Provide helpful, structured, and concise answers."),
                            Map.of("role", "user", "content", prompt)
                    ),
                    "temperature", 0.7,
                    "max_tokens", 1024
            );

            GroqResponse response = webClient.post()
                    .uri("/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(GroqResponse.class)
                    .timeout(Duration.ofSeconds(15))
                    .block();

            if (response != null && response.choices != null && !response.choices.isEmpty()) {
                return response.choices.get(0).message.content;
            }

            return "Error: Received empty response from Groq Cloud API.";
        } catch (Exception e) {
            logger.error("Groq Cloud API call failed: {}", e.getMessage(), e);
            return "Error calling Groq Cloud API: " + e.getMessage();
        }
    }

    private static class GroqResponse {
        @JsonProperty("choices")
        List<Choice> choices;
    }

    private static class Choice {
        @JsonProperty("message")
        Message message;
    }

    private static class Message {
        @JsonProperty("content")
        String content;
    }
}
