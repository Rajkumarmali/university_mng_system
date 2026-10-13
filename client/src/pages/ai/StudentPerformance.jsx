import React, { useState } from 'react';
import './StudentPerformance.css';

const StudentPerformance = () => {
    const [formData, setFormData] = useState({
        attendanceRate: 75,
        averageGrade: 70,
        assignmentsCompleted: 8,
        totalAssignments: 10,
        studyHours: 5,
        previousSemesterGPA: 3
    });
    const [prediction, setPrediction] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: parseFloat(e.target.value)
        });
    };

    const handlePredict = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8080/api/ai/performance/predict', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData)
            });

            const data = await response.json();
            setPrediction(data);
        } catch (error) {
            console.error('Error predicting performance:', error);
        } finally {
            setLoading(false);
        }
    };

    const getRiskColor = (riskLevel) => {
        switch (riskLevel) {
            case 'LOW': return '#28a745';
            case 'MEDIUM': return '#ffc107';
            case 'HIGH': return '#dc3545';
            default: return '#6c757d';
        }
    };

    return (
        <div className="performance-container">
            <h2>📊 Student Performance Prediction</h2>

            <div className="performance-grid">
                <div className="performance-form">
                    <h3>Enter Student Data</h3>

                    <div className="form-group">
                        <label>Attendance Rate (%)</label>
                        <input
                            type="range"
                            name="attendanceRate"
                            min="0"
                            max="100"
                            value={formData.attendanceRate}
                            onChange={handleChange}
                        />
                        <span>{formData.attendanceRate}%</span>
                    </div>

                    <div className="form-group">
                        <label>Average Grade (%)</label>
                        <input
                            type="range"
                            name="averageGrade"
                            min="0"
                            max="100"
                            value={formData.averageGrade}
                            onChange={handleChange}
                        />
                        <span>{formData.averageGrade}%</span>
                    </div>

                    <div className="form-group">
                        <label>Assignments Completed</label>
                        <input
                            type="number"
                            name="assignmentsCompleted"
                            value={formData.assignmentsCompleted}
                            onChange={handleChange}
                            min="0"
                        />
                    </div>

                    <div className="form-group">
                        <label>Total Assignments</label>
                        <input
                            type="number"
                            name="totalAssignments"
                            value={formData.totalAssignments}
                            onChange={handleChange}
                            min="1"
                        />
                    </div>

                    <div className="form-group">
                        <label>Daily Study Hours</label>
                        <input
                            type="range"
                            name="studyHours"
                            min="0"
                            max="12"
                            step="0.5"
                            value={formData.studyHours}
                            onChange={handleChange}
                        />
                        <span>{formData.studyHours} hours</span>
                    </div>

                    <div className="form-group">
                        <label>Previous Semester GPA (0-4)</label>
                        <input
                            type="range"
                            name="previousSemesterGPA"
                            min="0"
                            max="4"
                            step="0.1"
                            value={formData.previousSemesterGPA}
                            onChange={handleChange}
                        />
                        <span>{formData.previousSemesterGPA}</span>
                    </div>

                    <button onClick={handlePredict} disabled={loading} className="predict-btn">
                        {loading ? 'Analyzing...' : 'Predict Performance'}
                    </button>
                </div>

                {prediction && (
                    <div className="prediction-result">
                        <h3>Prediction Results</h3>

                        <div className="result-card">
                            <div className="result-item">
                                <label>Predicted Grade:</label>
                                <span className="grade-badge">{prediction.predictedGrade}</span>
                            </div>

                            <div className="result-item">
                                <label>Performance Score:</label>
                                <span>{prediction.performanceScore.toFixed(1)}%</span>
                            </div>

                            <div className="result-item">
                                <label>Risk Level:</label>
                                <span
                                    className="risk-badge"
                                    style={{ backgroundColor: getRiskColor(prediction.riskLevel) }}
                                >
                                    {prediction.riskLevel}
                                </span>
                            </div>

                            <div className="result-item">
                                <label>At Risk Student:</label>
                                <span>{prediction.atRiskStudent ? '⚠️ Yes' : '✅ No'}</span>
                            </div>

                            <div className="recommendations">
                                <h4>Recommendations:</h4>
                                <ul>
                                    {prediction.recommendations.map((rec, index) => (
                                        <li key={index}>{rec}</li>
                                    ))}
                                </ul>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default StudentPerformance;
