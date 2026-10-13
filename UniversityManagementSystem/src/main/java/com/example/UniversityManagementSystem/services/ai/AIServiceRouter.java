package com.example.UniversityManagementSystem.services.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Primary AI Service dispatcher that routes requests to either Groq Cloud API
 * or local Ollama depending on configuration in application.properties.
 */
@Service
@Primary
public class AIServiceRouter implements AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIServiceRouter.class);

    @Value("${ai.provider:groq}")
    private String provider;

    private final GroqCloudService groqCloudService;
    private final OllamaService ollamaService;

    @Autowired
    public AIServiceRouter(GroqCloudService groqCloudService, OllamaService ollamaService) {
        this.groqCloudService = groqCloudService;
        this.ollamaService = ollamaService;
    }

    @Override
    public String generateResponse(String prompt) {
        if ("groq".equalsIgnoreCase(provider)) {
            if (groqCloudService.isConfigured()) {
                logger.debug("Routing AI request to Groq Cloud API");
                return groqCloudService.generateResponse(prompt);
            } else {
                logger.warn("Groq API key is not configured. Falling back to Ollama or prompt instructions.");
                return groqCloudService.generateResponse(prompt);
            }
        }

        logger.debug("Routing AI request to local Ollama");
        return ollamaService.generateResponse(prompt);
    }
}
