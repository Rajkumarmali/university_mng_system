package com.example.UniversityManagementSystem.controller;

import com.example.UniversityManagementSystem.dto.ai.CourseRecommendationRequest;
import com.example.UniversityManagementSystem.services.ai.CourseRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/recommendations")
@CrossOrigin(origins = "*")
public class CourseRecommendationController {

    private final CourseRecommendationService recommendationService;

    @Autowired
    public CourseRecommendationController(CourseRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/courses")
    public ResponseEntity<List<CourseRecommendationService.CourseRecommendation>> getCourseRecommendations(
            @RequestBody CourseRecommendationRequest request) {
        try {
            Map<String, Object> studentProfile = new java.util.HashMap<>();
            studentProfile.put("interests", request.getInterests());
            studentProfile.put("completedCourses", request.getCompletedCourses());
            studentProfile.put("careerGoals", request.getCareerGoals());
            studentProfile.put("currentGPA", request.getCurrentGPA());
            studentProfile.put("preferredDifficulty", request.getPreferredDifficulty());

            List<CourseRecommendationService.CourseRecommendation> recommendations =
                    recommendationService.getRecommendations(studentProfile);

            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/career-path")
    public ResponseEntity<CourseRecommendationService.CareerPathAnalysis> analyzeCareerPath(
            @RequestBody Map<String, Object> request) {
        try {
            String careerGoal = (String) request.get("careerGoal");
            @SuppressWarnings("unchecked")
            List<String> completedCourses = (List<String>) request.get("completedCourses");

            CourseRecommendationService.CareerPathAnalysis analysis =
                    recommendationService.analyzeCareerPath(careerGoal, completedCourses);

            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/skill-gap")
    public ResponseEntity<CourseRecommendationService.SkillGapAnalysis> analyzeSkillGaps(
            @RequestBody Map<String, Object> request) {
        try {
            String careerGoal = (String) request.get("careerGoal");
            @SuppressWarnings("unchecked")
            List<String> currentSkills = (List<String>) request.get("currentSkills");

            CourseRecommendationService.SkillGapAnalysis analysis =
                    recommendationService.analyzeSkillGaps(careerGoal, currentSkills);

            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
