package com.example.UniversityManagementSystem.controller;

import com.example.UniversityManagementSystem.dto.ai.ExamQuestionRequest;
import com.example.UniversityManagementSystem.services.ai.ExamQuestionGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/exam-questions")
@CrossOrigin(origins = "*")
public class ExamQuestionGeneratorController {

    private final ExamQuestionGeneratorService questionGeneratorService;

    @Autowired
    public ExamQuestionGeneratorController(ExamQuestionGeneratorService questionGeneratorService) {
        this.questionGeneratorService = questionGeneratorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<List<ExamQuestionGeneratorService.ExamQuestion>> generateQuestions(
            @RequestBody ExamQuestionRequest request) {
        try {
            List<ExamQuestionGeneratorService.ExamQuestion> questions =
                    questionGeneratorService.generateQuestions(
                            request.getSubject(),
                            request.getTopic(),
                            request.getDifficulty(),
                            request.getCount(),
                            request.getQuestionType()
                    );
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/generate-paper")
    public ResponseEntity<Map<String, String>> generateQuestionPaper(
            @RequestBody Map<String, Object> request) {
        try {
            String subject = (String) request.get("subject");
            String topic = (String) request.get("topic");
            String difficulty = (String) request.get("difficulty");
            int multipleChoiceCount = ((Number) request.getOrDefault("multipleChoiceCount", 5)).intValue();
            int trueFalseCount = ((Number) request.getOrDefault("trueFalseCount", 5)).intValue();
            int shortAnswerCount = ((Number) request.getOrDefault("shortAnswerCount", 5)).intValue();
            int essayCount = ((Number) request.getOrDefault("essayCount", 2)).intValue();

            String questionPaper = questionGeneratorService.generateQuestionPaper(
                    subject, topic, difficulty,
                    multipleChoiceCount, trueFalseCount,
                    shortAnswerCount, essayCount
            );

            Map<String, String> response = new java.util.HashMap<>();
            response.put("questionPaper", questionPaper);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new java.util.HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, String>> validateQuestion(
            @RequestBody Map<String, String> request) {
        try {
            String questionText = request.get("questionText");
            String questionType = request.get("questionType");

            String feedback = questionGeneratorService.validateQuestion(questionText, questionType);

            Map<String, String> response = new java.util.HashMap<>();
            response.put("feedback", feedback);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new java.util.HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
