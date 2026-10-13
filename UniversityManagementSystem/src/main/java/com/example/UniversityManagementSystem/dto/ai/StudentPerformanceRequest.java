package com.example.UniversityManagementSystem.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentPerformanceRequest {
    private Long studentId;
    private double attendanceRate;
    private double averageGrade;
    private int assignmentsCompleted;
    private int totalAssignments;
    private double studyHours;
    private int previousSemesterGPA;
}
