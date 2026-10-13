package com.example.UniversityManagementSystem.controller;

import com.example.UniversityManagementSystem.dto.ai.ChatbotRequest;
import com.example.UniversityManagementSystem.dto.ai.ChatbotResponse;
import com.example.UniversityManagementSystem.services.ai.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/ai/chatbot")
@CrossOrigin(origins = "*")
public class AIChatbotController {

    private final ChatbotService chatbotService;

    @Autowired
    public AIChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatbotResponse> chat(@RequestBody ChatbotRequest request) {
        try {
            String aiResponse = chatbotService.getChatbotResponse(
                    request.getMessage(),
                    request.getUserType(),
                    request.getUserId()
            );

            ChatbotResponse response = new ChatbotResponse(
                    aiResponse,
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ChatbotResponse errorResponse = new ChatbotResponse(
                    "Sorry, I encountered an error processing your request. Please try again.",
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/course-info")
    public ResponseEntity<ChatbotResponse> getCourseInfo(@RequestParam String courseName) {
        try {
            String info = chatbotService.getCourseInformation(courseName);
            ChatbotResponse response = new ChatbotResponse(
                    info,
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ChatbotResponse errorResponse = new ChatbotResponse(
                    "Error retrieving course information",
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/fee-info")
    public ResponseEntity<ChatbotResponse> getFeeInfo(@RequestParam String courseName) {
        try {
            String info = chatbotService.getFeeInformation(courseName);
            ChatbotResponse response = new ChatbotResponse(
                    info,
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ChatbotResponse errorResponse = new ChatbotResponse(
                    "Error retrieving fee information",
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/study-advice")
    public ResponseEntity<ChatbotResponse> getStudyAdvice(
            @RequestParam String subject,
            @RequestParam(required = false) String studentLevel) {
        try {
            String advice = chatbotService.getStudyAdvice(subject, studentLevel != null ? studentLevel : "undergraduate");
            ChatbotResponse response = new ChatbotResponse(
                    advice,
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ChatbotResponse errorResponse = new ChatbotResponse(
                    "Error retrieving study advice",
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
