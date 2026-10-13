import React, { useState } from 'react';
import './ExamQuestionGenerator.css';

const ExamQuestionGenerator = () => {
    const [formData, setFormData] = useState({
        subject: '',
        topic: '',
        difficulty: 'medium',
        count: 5,
        questionType: 'multiple choice'
    });
    const [questions, setQuestions] = useState([]);
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleGenerate = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8080/api/ai/exam-questions/generate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData)
            });

            const data = await response.json();
            setQuestions(data);
        } catch (error) {
            console.error('Error generating questions:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleGeneratePaper = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8080/api/ai/exam-questions/generate-paper', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    subject: formData.subject,
                    topic: formData.topic,
                    difficulty: formData.difficulty,
                    multipleChoiceCount: 5,
                    trueFalseCount: 5,
                    shortAnswerCount: 5,
                    essayCount: 2
                })
            });

            const data = await response.json();
            const blob = new Blob([data.questionPaper], { type: 'text/plain' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `${formData.subject}_QuestionPaper.txt`;
            a.click();
            URL.revokeObjectURL(url);
        } catch (error) {
            console.error('Error generating paper:', error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="exam-generator-container">
            <h2>📝 AI Exam Question Generator</h2>

            <div className="exam-generator-grid">
                <div className="generator-form">
                    <h3>Question Parameters</h3>

                    <div className="form-group">
                        <label>Subject</label>
                        <input
                            type="text"
                            name="subject"
                            value={formData.subject}
                            onChange={handleChange}
                            placeholder="e.g., Computer Science"
                        />
                    </div>

                    <div className="form-group">
                        <label>Topic</label>
                        <input
                            type="text"
                            name="topic"
                            value={formData.topic}
                            onChange={handleChange}
                            placeholder="e.g., Data Structures"
                        />
                    </div>

                    <div className="form-group">
                        <label>Difficulty</label>
                        <select
                            name="difficulty"
                            value={formData.difficulty}
                            onChange={handleChange}
                        >
                            <option value="easy">Easy</option>
                            <option value="medium">Medium</option>
                            <option value="hard">Hard</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Number of Questions</label>
                        <input
                            type="number"
                            name="count"
                            value={formData.count}
                            onChange={handleChange}
                            min="1"
                            max="20"
                        />
                    </div>

                    <div className="form-group">
                        <label>Question Type</label>
                        <select
                            name="questionType"
                            value={formData.questionType}
                            onChange={handleChange}
                        >
                            <option value="multiple choice">Multiple Choice</option>
                            <option value="true/false">True/False</option>
                            <option value="short answer">Short Answer</option>
                            <option value="essay">Essay</option>
                        </select>
                    </div>

                    <div className="button-group">
                        <button onClick={handleGenerate} disabled={loading} className="generate-btn">
                            {loading ? 'Generating...' : 'Generate Questions'}
                        </button>
                        <button onClick={handleGeneratePaper} disabled={loading} className="paper-btn">
                            {loading ? 'Creating...' : 'Generate Full Paper'}
                        </button>
                    </div>
                </div>

                <div className="questions-display">
                    <h3>Generated Questions</h3>

                    {questions.length === 0 ? (
                        <p className="placeholder">No questions generated yet. Fill the form and click Generate.</p>
                    ) : (
                        <div className="questions-list">
                            {questions.map((question, index) => (
                                <div key={index} className="question-card">
                                    <div className="question-header">
                                        <span className="question-number">Q{index + 1}</span>
                                        <span className="question-type">{question.questionType}</span>
                                    </div>
                                    <div className="question-text">
                                        {question.questionText}
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

export default ExamQuestionGenerator;
