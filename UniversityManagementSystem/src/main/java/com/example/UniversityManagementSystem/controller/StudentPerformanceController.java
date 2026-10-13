package com.example.UniversityManagementSystem.controller;

import com.example.UniversityManagementSystem.dto.ai.StudentPerformanceRequest;
import com.example.UniversityManagementSystem.services.ai.StudentPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/performance")
@CrossOrigin(origins = "*")
public class StudentPerformanceController {

    private final StudentPerformanceService performanceService;

    @Autowired
    public StudentPerformanceController(StudentPerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @PostMapping("/predict")
    public ResponseEntity<StudentPerformanceService.PerformancePrediction> predictPerformance(
            @RequestBody StudentPerformanceRequest request) {
        try {
            Map<String, Object> studentData = new java.util.HashMap<>();
            studentData.put("attendanceRate", request.getAttendanceRate());
            studentData.put("averageGrade", request.getAverageGrade());
            studentData.put("assignmentsCompleted", request.getAssignmentsCompleted());
            studentData.put("totalAssignments", request.getTotalAssignments());
            studentData.put("studyHours", request.getStudyHours());
            studentData.put("previousSemesterGPA", request.getPreviousSemesterGPA());

            StudentPerformanceService.PerformancePrediction prediction =
                    performanceService.predictPerformance(studentData);

            return ResponseEntity.ok(prediction);
        } catch (Exception e) {
            StudentPerformanceService.PerformancePrediction errorPrediction =
                    new StudentPerformanceService.PerformancePrediction();
            errorPrediction.setError("Error processing prediction: " + e.getMessage());
            return ResponseEntity.status(500).body(errorPrediction);
        }
    }

    @PostMapping("/analyze-class")
    public ResponseEntity<Map<String, Object>> analyzeClassPerformance(
            @RequestBody List<StudentPerformanceRequest> classData) {
        try {
            List<Map<String, Object>> studentDataList = new java.util.ArrayList<>();
            for (StudentPerformanceRequest req : classData) {
                Map<String, Object> studentData = new java.util.HashMap<>();
                studentData.put("attendanceRate", req.getAttendanceRate());
                studentData.put("averageGrade", req.getAverageGrade());
                studentData.put("assignmentsCompleted", req.getAssignmentsCompleted());
                studentData.put("totalAssignments", req.getTotalAssignments());
                studentData.put("studyHours", req.getStudyHours());
                studentData.put("previousSemesterGPA", req.getPreviousSemesterGPA());
                studentDataList.add(studentData);
            }

            Map<String, Object> analysis = performanceService.analyzeClassPerformance(studentDataList);
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            Map<String, Object> error = new java.util.HashMap<>();
            error.put("error", "Error analyzing class: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
