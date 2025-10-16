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
    timeLimitHours: '24', // Default 24 hours
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.title.trim() || !formData.question.trim()) {
      setError('Title and question are required');
      return;
    }

    setLoading(true);
    setError('');

    try {
      // Calculate end time based on user selection
      let endTime: Date | null = null;
      
      if (formData.endTime) {
        // User selected specific date
        endTime = new Date(formData.endTime);
      } else if (formData.timeLimitHours) {
        // User selected time limit in hours
        const hours = parseInt(formData.timeLimitHours);
        endTime = new Date(Date.now() + (hours * 60 * 60 * 1000));
      }
      
      await createForum({
        title: formData.title.trim(),
        description: formData.description.trim(),
        question: formData.question.trim(),
        creatorId: user?.uid || '',
        creatorName: user?.displayName || user?.email?.split('@')[0] || 'Anonymous',
        endTime: endTime ? Timestamp.fromDate(endTime) : undefined,
        isActive: true,
        isAdminDeleted: false, // Track if admin manually deleted
      });

      navigate('/forums');
    } catch (err) {
      setError('Failed to create forum. Please try again.');
      console.error('Error creating forum:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    
    // Clear endTime if user selects time limit, and vice versa
    if (name === 'timeLimitHours' && value) {
      setFormData(prev => ({ ...prev, [name]: value, endTime: '' }));
    } else if (name === 'endTime' && value) {
      setFormData(prev => ({ ...prev, [name]: value, timeLimitHours: '' }));
    } else {
      setFormData(prev => ({ ...prev, [name]: value }));
    }
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
          <label>Forum Duration</label>
          <div className="duration-options">
            <div className="duration-option">
              <label htmlFor="timeLimitHours">Quick Duration:</label>
              <select
                id="timeLimitHours"
                name="timeLimitHours"
                value={formData.timeLimitHours}
                onChange={handleChange}
              >
                <option value="">Select duration...</option>
                <option value="1">1 hour</option>
                <option value="6">6 hours</option>
                <option value="12">12 hours</option>
                <option value="24">24 hours (1 day)</option>
                <option value="72">3 days</option>
                <option value="168">1 week</option>
                <option value="336">2 weeks</option>
              </select>
            </div>
            
            <div className="duration-divider">OR</div>
            
            <div className="duration-option">
              <label htmlFor="endTime">Custom End Date:</label>
              <input
                type="datetime-local"
                id="endTime"
                name="endTime"
                value={formData.endTime}
                onChange={handleChange}
                min={new Date().toISOString().slice(0, 16)}
              />
            </div>
          </div>
          <small>
            Choose how long the forum should stay open for discussions. 
            Leave both empty for permanent forum (admin can still close manually).
          </small>
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
