package com.example.UniversityManagementSystem.services.ai;

import com.example.UniversityManagementSystem.config.OllamaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService implements AIService {

    private final WebClient webClient;
    private final String model;

    @Autowired
    public OllamaService(WebClient webClient, OllamaConfig ollamaConfig) {
        this.webClient = webClient;
        this.model = ollamaConfig.getModel();
    }

    @Override
    public String generateResponse(String prompt) {
        try {
            OllamaRequest request = new OllamaRequest(model, prompt);
            OllamaResponse response = webClient.post()
                    .uri("/api/generate")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(OllamaResponse.class)
                    .timeout(Duration.ofSeconds(45))
                    .block();

            return response != null ? response.getResponse() : "Error: No response from Ollama";
        } catch (Exception e) {
            return "Error calling Ollama: " + e.getMessage();
        }
    }

    public String generateResponseAsync(String prompt) {
        return generateResponse(prompt);
    }

    private static class OllamaRequest {
        private String model;
        private String prompt;
        private boolean stream = false;
        private Map<String, Object> options;

        public OllamaRequest(String model, String prompt) {
            this.model = model;
            this.prompt = prompt;
            // Optimize for low-RAM machines: limit context and output token length
            this.options = new HashMap<>();
            this.options.put("num_ctx", 2048);
            this.options.put("num_predict", 350);
            this.options.put("temperature", 0.7);
        }

        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }

        public String getPrompt() { return prompt; }
        public void setPrompt(String prompt) { this.prompt = prompt; }

        public boolean isStream() { return stream; }
        public void setStream(boolean stream) { this.stream = stream; }

        public Map<String, Object> getOptions() { return options; }
        public void setOptions(Map<String, Object> options) { this.options = options; }
    }

    private static class OllamaResponse {
        private String response;
        private boolean done;

        public String getResponse() { return response; }
        public void setResponse(String response) { this.response = response; }

        public boolean isDone() { return done; }
        public void setDone(boolean done) { this.done = done; }
    }
}
