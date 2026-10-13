package com.example.UniversityManagementSystem.services.ai;

/**
 * Common interface for AI services (Groq Cloud API, Ollama Local, etc.).
 */
public interface AIService {
    String generateResponse(String prompt);
}
