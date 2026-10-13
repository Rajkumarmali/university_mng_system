package com.example.UniversityManagementSystem.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionRequest {
    private String subject;
    private String topic;
    private String difficulty;
    private int count;
    private String questionType;
}
