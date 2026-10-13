package com.example.UniversityManagementSystem.services.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    private final AIService aiService;

    @Autowired
    public ChatbotService(AIService aiService) {
        this.aiService = aiService;
    }

    public String getChatbotResponse(String userMessage, String userType, Long userId) {
        String systemPrompt = buildSystemPrompt(userType, userId);
        String fullPrompt = systemPrompt + "\n\nUser: " + userMessage;

        return aiService.generateResponse(fullPrompt);
    }

    private String buildSystemPrompt(String userType, Long userId) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an intelligent university management assistant. ");
        prompt.append("Your role is to help students, teachers, and administrators with university-related queries. ");
        prompt.append("Provide accurate, helpful, and concise responses. ");
        prompt.append("If you don't know the answer, suggest contacting the appropriate department. ");

        switch (userType.toLowerCase()) {
            case "student":
                prompt.append("\n\nYou are assisting a student. ");
                prompt.append("You can help with queries about courses, fees, exam schedules, attendance, grades, ");
                prompt.append("scholarships, and general university information. ");
                prompt.append("Student ID: ").append(userId);
                break;
            case "teacher":
                prompt.append("\n\nYou are assisting a teacher. ");
                prompt.append("You can help with queries about classes, students, exams, grading, ");
                prompt.append("attendance management, and department information. ");
                prompt.append("Teacher ID: ").append(userId);
                break;
            case "admin":
                prompt.append("\n\nYou are assisting an administrator. ");
                prompt.append("You can help with queries about university management, departments, ");
                prompt.append("courses, faculty, students, fees, and administrative tasks. ");
                prompt.append("Admin ID: ").append(userId);
                break;
            default:
                prompt.append("\n\nYou are assisting a general user. ");
                prompt.append("Provide general university information and guidance. ");
        }

        return prompt.toString();
    }

    public String getCourseInformation(String courseName) {
        String prompt = "Provide detailed information about the course: " + courseName + ". " +
                "Include typical subjects, duration, career opportunities, and prerequisites.";
        return aiService.generateResponse(prompt);
    }

    public String getFeeInformation(String courseName) {
        String prompt = "Provide detailed fee structure information for the course: " + courseName + ". " +
                "Include tuition fees, other charges, payment options, and scholarship availability.";
        return aiService.generateResponse(prompt);
    }

    public String getStudyAdvice(String subject, String studentLevel) {
        String prompt = "Provide study advice and tips for a " + studentLevel + " level student studying " + subject + ". " +
                "Include effective learning strategies, resources, and time management tips.";
        return aiService.generateResponse(prompt);
    }
}
