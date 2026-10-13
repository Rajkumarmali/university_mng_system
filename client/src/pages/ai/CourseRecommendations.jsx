import React, { useState } from 'react';
import './CourseRecommendations.css';

const CourseRecommendations = () => {
    const [formData, setFormData] = useState({
        interests: '',
        completedCourses: '',
        careerGoals: '',
        currentGPA: 3.0,
        preferredDifficulty: 'medium'
    });
    const [recommendations, setRecommendations] = useState([]);
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleGetRecommendations = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8080/api/ai/recommendations/courses', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    studentId: 1,
                    ...formData
                })
            });

            const data = await response.json();
            setRecommendations(data);
        } catch (error) {
            console.error('Error getting recommendations:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleCareerAnalysis = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8080/api/ai/recommendations/career-path', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    careerGoal: formData.careerGoals,
                    completedCourses: formData.completedCourses.split(',').map(c => c.trim())
                })
            });

            const data = await response.json();
            alert(data.analysis);
        } catch (error) {
            console.error('Error analyzing career path:', error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="recommendations-container">
            <h2>🎯 Smart Course Recommendations</h2>

            <div className="recommendations-grid">
                <div className="recommendations-form">
                    <h3>Your Profile</h3>

                    <div className="form-group">
                        <label>Interests</label>
                        <textarea
                            name="interests"
                            value={formData.interests}
                            onChange={handleChange}
                            placeholder="e.g., Programming, Data Science, Machine Learning"
                            rows="3"
                        />
                    </div>

                    <div className="form-group">
                        <label>Completed Courses</label>
                        <textarea
                            name="completedCourses"
                            value={formData.completedCourses}
                            onChange={handleChange}
                            placeholder="e.g., Introduction to Programming, Data Structures, Algorithms"
                            rows="3"
                        />
                    </div>

                    <div className="form-group">
                        <label>Career Goals</label>
                        <textarea
                            name="careerGoals"
                            value={formData.careerGoals}
                            onChange={handleChange}
                            placeholder="e.g., Software Engineer, Data Scientist, AI Researcher"
                            rows="2"
                        />
                    </div>

                    <div className="form-group">
                        <label>Current GPA (0-4)</label>
                        <input
                            type="range"
                            name="currentGPA"
                            min="0"
                            max="4"
                            step="0.1"
                            value={formData.currentGPA}
                            onChange={handleChange}
                        />
                        <span>{formData.currentGPA}</span>
                    </div>

                    <div className="form-group">
                        <label>Preferred Difficulty</label>
                        <select
                            name="preferredDifficulty"
                            value={formData.preferredDifficulty}
                            onChange={handleChange}
                        >
                            <option value="easy">Easy</option>
                            <option value="medium">Medium</option>
                            <option value="hard">Hard</option>
                        </select>
                    </div>

                    <div className="button-group">
                        <button
                            onClick={handleGetRecommendations}
                            disabled={loading}
                            className="recommend-btn"
                        >
                            {loading ? 'Analyzing...' : 'Get Recommendations'}
                        </button>
                        <button
                            onClick={handleCareerAnalysis}
                            disabled={loading}
                            className="career-btn"
                        >
                            {loading ? 'Analyzing...' : 'Analyze Career Path'}
                        </button>
                    </div>
                </div>

                <div className="recommendations-display">
                    <h3>Recommended Courses</h3>

                    {recommendations.length === 0 ? (
                        <p className="placeholder">
                            Fill your profile and click "Get Recommendations" to see personalized course suggestions.
                        </p>
                    ) : (
                        <div className="recommendations-list">
                            {recommendations.map((rec, index) => (
                                <div key={index} className="recommendation-card">
                                    <div className="rec-header">
                                        <h4>{rec.courseName}</h4>
                                        <span className={`difficulty-badge ${rec.difficulty}`}>
                                            {rec.difficulty}
                                        </span>
                                    </div>
                                    <p className="rec-description">{rec.description}</p>
                                    <div className="rec-details">
                                        <div className="rec-detail">
                                            <strong>Why it matches:</strong>
                                            <p>{rec.reason}</p>
                                        </div>
                                        <div className="rec-detail">
                                            <strong>Prerequisites:</strong>
                                            <p>{rec.prerequisites || 'None'}</p>
                                        </div>
                                        <div className="rec-detail">
                                            <strong>Career Relevance:</strong>
                                            <p>{rec.careerRelevance}</p>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default CourseRecommendations;
