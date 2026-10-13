import React, { useState } from 'react';
import './Chatbot.css';

const Chatbot = () => {
    const [messages, setMessages] = useState([
        { sender: 'bot', text: 'Hello! I\'m your University AI Assistant. How can I help you today?' }
    ]);
    const [input, setInput] = useState('');
    const [loading, setLoading] = useState(false);
    const [userType, setUserType] = useState('student');

    const handleSend = async () => {
        if (!input.trim()) return;

        const userMessage = { sender: 'user', text: input };
        setMessages([...messages, userMessage]);
        setInput('');
        setLoading(true);

        try {
            const response = await fetch('http://localhost:8080/api/ai/chatbot/chat', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + localStorage.getItem("token")
                },
                body: JSON.stringify({
                    message: input,
                    userType: userType,
                    userId: 1
                })
            });
            console.log('Response status:', response);
            const data = await response.json();
            const botMessage = { sender: 'bot', text: data.response };
            setMessages(prev => [...prev, botMessage]);
        } catch (error) {
            const errorMessage = { sender: 'bot', text: 'Sorry, I encountered an error. Please try again.' };
            setMessages(prev => [...prev, errorMessage]);
        } finally {
            setLoading(false);
        }
    };

    const handleQuickAction = async (action) => {
        setInput(action);
        await handleSend();
    };

    return (
        <div className="chatbot-container">
            <div className="chatbot-header">
                <h2>🎓 University AI Assistant</h2>
                <select
                    value={userType}
                    onChange={(e) => setUserType(e.target.value)}
                    className="user-type-select"
                >
                    <option value="student">Student</option>
                    <option value="teacher">Teacher</option>
                    <option value="admin">Administrator</option>
                </select>
            </div>

            <div className="chatbot-messages">
                {messages.map((msg, index) => (
                    <div key={index} className={`message ${msg.sender}`}>
                        <div className="message-content">{msg.text}</div>
                    </div>
                ))}
                {loading && <div className="message bot"><div className="message-content">Typing...</div></div>}
            </div>

            <div className="quick-actions">
                <button onClick={() => handleQuickAction('What courses are available?')}>
                    Available Courses
                </button>
                <button onClick={() => handleQuickAction('What are the fee structures?')}>
                    Fee Information
                </button>
                <button onClick={() => handleQuickAction('When are the exams scheduled?')}>
                    Exam Schedule
                </button>
                <button onClick={() => handleQuickAction('How can I improve my grades?')}>
                    Study Tips
                </button>
            </div>

            <div className="chatbot-input">
                <input
                    type="text"
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyPress={(e) => e.key === 'Enter' && handleSend()}
                    placeholder="Ask me anything about the university..."
                />
                <button onClick={handleSend} disabled={loading}>
                    Send
                </button>
            </div>
        </div>
    );
};

export default Chatbot;
