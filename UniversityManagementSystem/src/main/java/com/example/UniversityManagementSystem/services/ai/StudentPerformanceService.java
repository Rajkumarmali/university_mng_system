package com.example.UniversityManagementSystem.services.ai;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentPerformanceService {

    public PerformancePrediction predictPerformance(Map<String, Object> studentData) {
        PerformancePrediction prediction = new PerformancePrediction();

        try {
            double attendanceRate = ((Number) studentData.getOrDefault("attendanceRate", 0.0)).doubleValue();
            double averageGrade = ((Number) studentData.getOrDefault("averageGrade", 0.0)).doubleValue();
            int assignmentsCompleted = ((Number) studentData.getOrDefault("assignmentsCompleted", 0)).intValue();
            int totalAssignments = ((Number) studentData.getOrDefault("totalAssignments", 1)).intValue();
            double studyHours = ((Number) studentData.getOrDefault("studyHours", 0.0)).doubleValue();
            int previousSemesterGPA = ((Number) studentData.getOrDefault("previousSemesterGPA", 0)).intValue();

            double assignmentCompletionRate = totalAssignments > 0 ?
                    (double) assignmentsCompleted / totalAssignments : 0.0;

            double performanceScore = calculatePerformanceScore(
                    attendanceRate,
                    averageGrade,
                    assignmentCompletionRate,
                    studyHours,
                    previousSemesterGPA
            );

            prediction.setPredictedGrade(predictGrade(performanceScore));
            prediction.setPerformanceScore(performanceScore);
            prediction.setRiskLevel(determineRiskLevel(performanceScore));
            prediction.setRecommendations(generateRecommendations(performanceScore, attendanceRate, assignmentCompletionRate));
            prediction.setAtRiskStudent(prediction.getRiskLevel() != RiskLevel.LOW);

        } catch (Exception e) {
            prediction.setError("Error predicting performance: " + e.getMessage());
        }

        return prediction;
    }

    private double calculatePerformanceScore(double attendance, double avgGrade,
                                            double assignmentRate, double studyHours, int prevGPA) {
        double score = 0.0;

        score += attendance * 0.25;
        score += (avgGrade / 100.0) * 0.30;
        score += assignmentRate * 0.20;
        score += Math.min(studyHours / 10.0, 1.0) * 0.15;
        score += (prevGPA / 4.0) * 0.10;

        return Math.min(score * 100, 100);
    }

    private String predictGrade(double performanceScore) {
        if (performanceScore >= 90) return "A";
        if (performanceScore >= 80) return "B";
        if (performanceScore >= 70) return "C";
        if (performanceScore >= 60) return "D";
        return "F";
    }

    private RiskLevel determineRiskLevel(double performanceScore) {
        if (performanceScore >= 75) return RiskLevel.LOW;
        if (performanceScore >= 60) return RiskLevel.MEDIUM;
        return RiskLevel.HIGH;
    }

    private List<String> generateRecommendations(double performanceScore,
                                               double attendance, double assignmentRate) {
        List<String> recommendations = new ArrayList<>();

        if (attendance < 75) {
            recommendations.add("Improve attendance rate. Regular class attendance is crucial for success.");
        }

        if (assignmentRate < 80) {
            recommendations.add("Complete all assignments on time. Practice is key to understanding concepts.");
        }

        if (performanceScore < 70) {
            recommendations.add("Consider seeking additional help from teachers or tutors.");
            recommendations.add("Form study groups with classmates for collaborative learning.");
        }

        if (performanceScore < 60) {
            recommendations.add("Immediate intervention recommended. Meet with academic advisor.");
            recommendations.add("Review study habits and time management strategies.");
        }

        if (recommendations.isEmpty()) {
            recommendations.add("Keep up the good work! Maintain current performance levels.");
        }

        return recommendations;
    }

    public Map<String, Object> analyzeClassPerformance(List<Map<String, Object>> classData) {
        Map<String, Object> analysis = new HashMap<>();

        if (classData.isEmpty()) {
            analysis.put("error", "No data provided");
            return analysis;
        }

        List<Double> performanceScores = new ArrayList<>();
        for (Map<String, Object> data : classData) {
            performanceScores.add(predictPerformance(data).getPerformanceScore());
        }

        double average = 0.0;
        double max = 0.0;
        double min = 0.0;

        if (!performanceScores.isEmpty()) {
            average = performanceScores.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            max = performanceScores.stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(0.0);

            min = performanceScores.stream()
                    .mapToDouble(Double::doubleValue)
                    .min()
                    .orElse(0.0);
        }

        long atRiskCount = 0;
        for (Map<String, Object> data : classData) {
            if (predictPerformance(data).isAtRiskStudent()) {
                atRiskCount++;
            }
        }

        analysis.put("averagePerformance", average);
        analysis.put("highestPerformance", max);
        analysis.put("lowestPerformance", min);
        analysis.put("atRiskStudents", atRiskCount);
        analysis.put("totalStudents", classData.size());
        analysis.put("atRiskPercentage", classData.size() > 0 ? (atRiskCount * 100.0) / classData.size() : 0.0);

        return analysis;
    }

    public enum RiskLevel {
        LOW, MEDIUM, HIGH
    }

    public static class PerformancePrediction {
        private String predictedGrade;
        private double performanceScore;
        private RiskLevel riskLevel;
        private List<String> recommendations;
        private boolean atRiskStudent;
        private String error;

        public PerformancePrediction() {
            this.recommendations = new ArrayList<>();
        }

        public String getPredictedGrade() { return predictedGrade; }
        public void setPredictedGrade(String predictedGrade) { this.predictedGrade = predictedGrade; }

        public double getPerformanceScore() { return performanceScore; }
        public void setPerformanceScore(double performanceScore) { this.performanceScore = performanceScore; }

        public RiskLevel getRiskLevel() { return riskLevel; }
        public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }

        public List<String> getRecommendations() { return recommendations; }
        public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }

        public boolean isAtRiskStudent() { return atRiskStudent; }
        public void setAtRiskStudent(boolean atRiskStudent) { this.atRiskStudent = atRiskStudent; }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}
