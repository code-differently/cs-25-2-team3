import { Timestamp } from "firebase/firestore";
import { useState } from "react";
import { useNavigate } from "react-router";
import { useAuthState } from "../hooks/useAuthState";
import { useFirestore } from "../hooks/useFirestore";
import "./forum.css";

export default function CreateForum() {
  const navigate = useNavigate();
  const { user, isAuthenticated } = useAuthState();
  const { createForum } = useFirestore();
  
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    question: '',
    endTime: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  // Redirect if not authenticated
  if (!isAuthenticated) {
    navigate('/');
    return null;
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.title.trim() || !formData.question.trim()) {
      setError('Title and question are required');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const endTime = formData.endTime ? new Date(formData.endTime) : null;
      
      await createForum({
        title: formData.title.trim(),
        description: formData.description.trim(),
        question: formData.question.trim(),
        creatorId: user?.uid || '',
        creatorName: user?.displayName || user?.email?.split('@')[0] || 'Anonymous',
        endTime: endTime ? Timestamp.fromDate(new Date(endTime.getTime() + 24 * 60 * 60 * 1000)) : undefined,
        isActive: true,
      });

      navigate('/forums');
    } catch (err) {
      setError('Failed to create forum. Please try again.');
      console.error('Error creating forum:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  };

  return (
    <div className="create-forum-container">
      <div className="create-forum-header">
        <button 
          onClick={() => navigate('/forums')} 
          className="back-btn"
        >
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <path d="M19 12H5M12 19l-7-7 7-7" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
          Back to Forums
        </button>
        <h1>Create New Forum</h1>
      </div>

      <form onSubmit={handleSubmit} className="create-forum-form">
        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        <div className="form-group">
          <label htmlFor="title">Forum Title *</label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            placeholder="What's your forum about?"
            maxLength={100}
            required
          />
          <small>{formData.title.length}/100 characters</small>
        </div>

        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            placeholder="Provide some context or background information..."
            rows={4}
            maxLength={500}
          />
          <small>{formData.description.length}/500 characters</small>
        </div>

        <div className="form-group">
          <label htmlFor="question">Main Question *</label>
          <textarea
            id="question"
            name="question"
            value={formData.question}
            onChange={handleChange}
            placeholder="What specific question do you want the community to discuss?"
            rows={3}
            maxLength={300}
            required
          />
          <small>{formData.question.length}/300 characters</small>
        </div>

        <div className="form-group">
          <label htmlFor="endTime">Discussion End Date (Optional)</label>
          <input
            type="date"
            id="endTime"
            name="endTime"
            value={formData.endTime}
            onChange={handleChange}
            min={new Date().toISOString().split('T')[0]}
          />
          <small>Leave empty for no end date. Forums will auto-close after 7 days by default.</small>
        </div>

        <div className="form-actions">
          <button 
            type="button" 
            onClick={() => navigate('/forums')}
            className="cancel-btn"
            disabled={loading}
          >
            Cancel
          </button>
          <button 
            type="submit" 
            className="submit-btn"
            disabled={loading}
          >
            {loading ? 'Creating...' : 'Create Forum'}
          </button>
        </div>
      </form>
    </div>
  );
}
