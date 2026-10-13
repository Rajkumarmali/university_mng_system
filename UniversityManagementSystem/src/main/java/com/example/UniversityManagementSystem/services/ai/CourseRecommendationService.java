package com.example.UniversityManagementSystem.services.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CourseRecommendationService {

    private final AIService aiService;

    @Autowired
    public CourseRecommendationService(AIService aiService) {
        this.aiService = aiService;
    }

    public List<CourseRecommendation> getRecommendations(Map<String, Object> studentProfile) {
        List<CourseRecommendation> recommendations = new ArrayList<>();

        try {
            String interests = (String) studentProfile.getOrDefault("interests", "");
            String completedCourses = (String) studentProfile.getOrDefault("completedCourses", "");
            String careerGoals = (String) studentProfile.getOrDefault("careerGoals", "");
            double currentGPA = ((Number) studentProfile.getOrDefault("currentGPA", 0.0)).doubleValue();
            String preferredDifficulty = (String) studentProfile.getOrDefault("preferredDifficulty", "medium");

            String prompt = buildRecommendationPrompt(interests, completedCourses, careerGoals,
                    currentGPA, preferredDifficulty);

            String response = aiService.generateResponse(prompt);
            recommendations = parseRecommendations(response);

        } catch (Exception e) {
            CourseRecommendation errorRec = new CourseRecommendation();
            errorRec.setCourseName("Error");
            errorRec.setReason("Error generating recommendations: " + e.getMessage());
            recommendations.add(errorRec);
        }

        return recommendations;
    }

    private String buildRecommendationPrompt(String interests, String completedCourses,
                                             String careerGoals, double currentGPA,
                                             String preferredDifficulty) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Based on the following student profile, recommend 5 suitable courses:\n\n");
        prompt.append("Student Interests: ").append(interests).append("\n");
        prompt.append("Completed Courses: ").append(completedCourses).append("\n");
        prompt.append("Career Goals: ").append(careerGoals).append("\n");
        prompt.append("Current GPA: ").append(currentGPA).append("\n");
        prompt.append("Preferred Difficulty: ").append(preferredDifficulty).append("\n\n");

        prompt.append("For each recommendation, provide:\n");
        prompt.append("1. Course Name\n");
        prompt.append("2. Brief Description\n");
        prompt.append("3. Why it matches the student's profile\n");
        prompt.append("4. Prerequisites (if any)\n");
        prompt.append("5. Difficulty level\n");
        prompt.append("6. Career relevance\n\n");

        prompt.append("Format each recommendation as:\n");
        prompt.append("COURSE: [course name]\n");
        prompt.append("DESCRIPTION: [description]\n");
        prompt.append("REASON: [why it matches]\n");
        prompt.append("PREREQUISITES: [prerequisites]\n");
        prompt.append("DIFFICULTY: [difficulty level]\n");
        prompt.append("CAREER_RELEVANCE: [how it helps career goals]\n\n");

        return prompt.toString();
    }

    private List<CourseRecommendation> parseRecommendations(String response) {
        List<CourseRecommendation> recommendations = new ArrayList<>();
        String[] courseBlocks = response.split("(?i)COURSE:");

        for (int i = 1; i < courseBlocks.length; i++) {
            CourseRecommendation rec = new CourseRecommendation();
            String block = courseBlocks[i];

            rec.setCourseName(extractValue(block, "", "\n"));
            rec.setDescription(extractValue(block, "DESCRIPTION:", "\n"));
            rec.setReason(extractValue(block, "REASON:", "\n"));
            rec.setPrerequisites(extractValue(block, "PREREQUISITES:", "\n"));
            rec.setDifficulty(extractValue(block, "DIFFICULTY:", "\n"));
            rec.setCareerRelevance(extractValue(block, "CAREER_RELEVANCE:", "\n"));

            recommendations.add(rec);
        }

        return recommendations;
    }

    private String extractValue(String text, String prefix, String delimiter) {
        int startIndex = text.indexOf(prefix);
        if (startIndex == -1) return "";
        startIndex += prefix.length();

        int endIndex = text.indexOf(delimiter, startIndex);
        if (endIndex == -1) endIndex = text.length();

        return text.substring(startIndex, endIndex).trim();
    }

    public CareerPathAnalysis analyzeCareerPath(String careerGoal, List<String> completedCourses) {
        CareerPathAnalysis analysis = new CareerPathAnalysis();

        String prompt = "Analyze the career path for a student interested in: " + careerGoal + "\n\n" +
                "Completed Courses: " + String.join(", ", completedCourses) + "\n\n" +
                "Provide:\n" +
                "1. Current progress toward this career\n" +
                "2. Recommended next courses (5 courses)\n" +
                "3. Skills needed to develop\n" +
                "4. Industry trends and opportunities\n" +
                "5. Estimated timeline to reach career goal\n" +
                "6. Additional certifications or experiences recommended";

        String response = aiService.generateResponse(prompt);
        analysis.setAnalysis(response);
        analysis.setCareerGoal(careerGoal);

        return analysis;
    }

    public SkillGapAnalysis analyzeSkillGaps(String careerGoal, List<String> currentSkills) {
        SkillGapAnalysis analysis = new SkillGapAnalysis();

        String prompt = "Analyze skill gaps for a student targeting career: " + careerGoal + "\n\n" +
                "Current Skills: " + String.join(", ", currentSkills) + "\n\n" +
                "Identify:\n" +
                "1. Skills the student already has (strengths)\n" +
                "2. Critical skills missing (skill gaps)\n" +
                "3. Recommended resources to learn missing skills\n" +
                "4. Priority order for skill development\n" +
                "5. Difficulty level for each missing skill";

        String response = aiService.generateResponse(prompt);
        analysis.setAnalysis(response);
        analysis.setCareerGoal(careerGoal);

        return analysis;
    }

    public static class CourseRecommendation {
        private String courseName;
        private String description;
        private String reason;
        private String prerequisites;
        private String difficulty;
        private String careerRelevance;
        private double matchScore;

        public CourseRecommendation() {
            this.matchScore = 0.0;
        }

        public String getCourseName() { return courseName; }
        public void setCourseName(String courseName) { this.courseName = courseName; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }

        public String getPrerequisites() { return prerequisites; }
        public void setPrerequisites(String prerequisites) { this.prerequisites = prerequisites; }

        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

        public String getCareerRelevance() { return careerRelevance; }
        public void setCareerRelevance(String careerRelevance) { this.careerRelevance = careerRelevance; }

        public double getMatchScore() { return matchScore; }
        public void setMatchScore(double matchScore) { this.matchScore = matchScore; }
    }

    public static class CareerPathAnalysis {
        private String careerGoal;
        private String analysis;

        public String getCareerGoal() { return careerGoal; }
        public void setCareerGoal(String careerGoal) { this.careerGoal = careerGoal; }

        public String getAnalysis() { return analysis; }
        public void setAnalysis(String analysis) { this.analysis = analysis; }
    }

    public static class SkillGapAnalysis {
        private String careerGoal;
        private String analysis;

        public String getCareerGoal() { return careerGoal; }
        public void setCareerGoal(String careerGoal) { this.careerGoal = careerGoal; }

        public String getAnalysis() { return analysis; }
        public void setAnalysis(String analysis) { this.analysis = analysis; }
    }
}
