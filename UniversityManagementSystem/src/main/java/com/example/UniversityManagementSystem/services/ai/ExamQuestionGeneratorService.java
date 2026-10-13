package com.example.UniversityManagementSystem.services.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ExamQuestionGeneratorService {

    private final AIService aiService;

    @Autowired
    public ExamQuestionGeneratorService(AIService aiService) {
        this.aiService = aiService;
    }

    public List<ExamQuestion> generateQuestions(String subject, String topic,
                                                 String difficulty, int count, String questionType) {
        String prompt = buildQuestionPrompt(subject, topic, difficulty, count, questionType);
        String response = aiService.generateResponse(prompt);
        return parseQuestionsFromResponse(response, questionType);
    }

    private String buildQuestionPrompt(String subject, String topic,
                                      String difficulty, int count, String questionType) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate ").append(count).append(" ").append(questionType).append(" questions ");
        prompt.append("for the subject '").append(subject).append("' ");
        prompt.append("on the topic '").append(topic).append("' ");
        prompt.append("with ").append(difficulty).append(" difficulty level.\n\n");

        switch (questionType.toLowerCase()) {
            case "multiple choice":
                prompt.append("Format each question as:\n");
                prompt.append("Question: [question text]\n");
                prompt.append("Options: A) [option 1], B) [option 2], C) [option 3], D) [option 4]\n");
                prompt.append("Correct Answer: [A/B/C/D]\n");
                prompt.append("Explanation: [brief explanation]\n\n");
                break;
            case "true/false":
                prompt.append("Format each question as:\n");
                prompt.append("Question: [question text]\n");
                prompt.append("Correct Answer: [True/False]\n");
                prompt.append("Explanation: [brief explanation]\n\n");
                break;
            case "short answer":
                prompt.append("Format each question as:\n");
                prompt.append("Question: [question text]\n");
                prompt.append("Expected Answer: [brief expected answer]\n");
                prompt.append("Explanation: [brief explanation]\n\n");
                break;
            case "essay":
                prompt.append("Format each question as:\n");
                prompt.append("Question: [question text]\n");
                prompt.append("Expected Length: [word count]\n");
                prompt.append("Key Points: [key points to cover]\n\n");
                break;
            default:
                prompt.append("Format each question clearly with the question, answer, and explanation.\n\n");
        }

        prompt.append("Ensure questions are clear, accurate, and appropriate for the difficulty level. ");
        prompt.append("Number each question starting from 1.");

        return prompt.toString();
    }

    private List<ExamQuestion> parseQuestionsFromResponse(String response, String questionType) {
        List<ExamQuestion> questions = new ArrayList<>();
        String[] parts = response.split("(?i)question \\d+[:\\.]");

        for (int i = 1; i < parts.length; i++) {
            ExamQuestion question = new ExamQuestion();
            question.setQuestionType(questionType);
            question.setQuestionText(parts[i].trim());
            questions.add(question);
        }

        return questions;
    }

    public String generateQuestionPaper(String subject, String topic, String difficulty,
                                        int multipleChoiceCount, int trueFalseCount,
                                        int shortAnswerCount, int essayCount) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a complete question paper for subject '").append(subject).append("' ");
        prompt.append("on topic '").append(topic).append("' with ").append(difficulty).append(" difficulty.\n\n");
        prompt.append("Include:\n");
        prompt.append("- ").append(multipleChoiceCount).append(" Multiple Choice Questions\n");
        prompt.append("- ").append(trueFalseCount).append(" True/False Questions\n");
        prompt.append("- ").append(shortAnswerCount).append(" Short Answer Questions\n");
        prompt.append("- ").append(essayCount).append(" Essay Questions\n\n");
        prompt.append("Format the paper professionally with clear sections and marks distribution.");
        prompt.append("Include an answer key at the end.");

        return aiService.generateResponse(prompt.toString());
    }

    public String validateQuestion(String questionText, String questionType) {
        String prompt = "Review the following " + questionType + " question for quality and accuracy:\n\n" +
                questionText + "\n\n" +
                "Provide feedback on:\n" +
                "1. Clarity and precision\n" +
                "2. Difficulty level appropriateness\n" +
                "3. Accuracy of content\n" +
                "4. Any improvements needed\n" +
                "5. Overall rating (1-10)";

        return aiService.generateResponse(prompt);
    }

    public static class ExamQuestion {
        private String questionType;
        private String questionText;
        private List<String> options;
        private String correctAnswer;
        private String explanation;
        private int marks;

        public ExamQuestion() {
            this.options = new ArrayList<>();
            this.marks = 1;
        }

        public String getQuestionType() { return questionType; }
        public void setQuestionType(String questionType) { this.questionType = questionType; }

        public String getQuestionText() { return questionText; }
        public void setQuestionText(String questionText) { this.questionText = questionText; }

        public List<String> getOptions() { return options; }
        public void setOptions(List<String> options) { this.options = options; }

        public String getCorrectAnswer() { return correctAnswer; }
        public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }

        public int getMarks() { return marks; }
        public void setMarks(int marks) { this.marks = marks; }
    }
}
