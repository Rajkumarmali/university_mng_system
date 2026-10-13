package com.example.UniversityManagementSystem.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRecommendationRequest {
    private Long studentId;
    private String interests;
    private String completedCourses;
    private String careerGoals;
    private double currentGPA;
    private String preferredDifficulty;
}
