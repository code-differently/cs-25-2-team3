import type { User } from "firebase/auth";
import { doc, getDoc } from "firebase/firestore";
import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router";
import { db } from "../firebase";
import { useAuthState } from "../hooks/useAuthState";
import { useComments, useFirestore, useForum, type Forum, type Comment } from "../hooks/useFirestore";
import "./forum.css";

export default function ForumDetail() {
  const { forumId } = useParams();
  const navigate = useNavigate();
  const { user, isAuthenticated } = useAuthState();
  const { forum, loading: forumLoading } = useForum(forumId || '');
  const { comments, loading: commentsLoading } = useComments(forumId || '');
  const { voteOnForum, createComment } = useFirestore();
  
  const [votingState, setVotingState] = useState<'upvote' | 'downvote' | null>(null);
  const [newComment, setNewComment] = useState('');
  const [commentLoading, setCommentLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchForum = async () => {
      if (!forumId) return;
      
      try {
        const forumDoc = await getDoc(doc(db, 'forums', forumId));
        if (forumDoc.exists()) {
          // setForum({ id: forumDoc.id, ...forumDoc.data() } as Forum);
        } else {
          navigate('/forums');
        }
      } catch (error) {
        console.error('Error fetching forum:', error);
        navigate('/forums');
      } finally {
        // setLoading(false);
      }
    };

    fetchForum();
  }, [forumId, navigate]);

  if (forumLoading) {
    return (
      <div className="forum-detail-container">
        <div className="loading-spinner">Loading forum...</div>
      </div>
    );
  }

  if (!forum) {
    return (
      <div className="forum-detail-container">
        <div className="error-message">Forum not found</div>
        <Link to="/forums" className="back-link">← Back to Forums</Link>
      </div>
    );
  }

  const formatDate = (timestamp: any) => {
    if (!timestamp) return '';
    const date = timestamp.toDate ? timestamp.toDate() : new Date(timestamp);
    const now = Date.now();
    const diffMs = now - date.getTime();
    const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));
    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
    const diffMinutes = Math.floor(diffMs / (1000 * 60));

    if (diffDays > 0) {
      return `${diffDays}d ago`;
    } else if (diffHours > 0) {
      return `${diffHours}h ago`;
    } else if (diffMinutes > 0) {
      return `${diffMinutes}m ago`;
    } else {

  if (loading) {
    return (
      <div className="forum-detail-container">
        <div className="loading-spinner">Loading forum...</div>
      </div>
    );
  }

  if (!forum) {
    return (
      <div className="forum-detail-container">
        <div className="error-state">Forum not found</div>
      </div>
    );
  }

  return (
    <div className="forum-detail-container">
      <div className="forum-detail-header">
        <button 
          onClick={() => navigate('/forums')} 
          className="back-btn"
        >
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <path d="M19 12H5M12 19l-7-7 7-7" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
          Back to Forums
        </button>
      </div>

      <div className="forum-detail-card">
        <div className="forum-detail-header-info">
          <div className="forum-creator">
            <div className="creator-avatar">
              {forum.creatorName ? forum.creatorName[0].toUpperCase() : '?'}
            </div>
            <div className="creator-info">
              <span className="creator-name">{forum.creatorName || 'Anonymous'}</span>
              <span className="post-time">{formatDate(forum.createdAt)}</span>
            </div>
          </div>
        </div>

        <div className="forum-detail-content">
          <h1 className="forum-detail-title">{forum.title}</h1>
          {forum.description && (
            <p className="forum-detail-description">{forum.description}</p>
          )}
          <div className="forum-detail-question">
            <strong>Question:</strong> {forum.question}
          </div>
        </div>

        <div className="forum-detail-actions">
          <div className="vote-section">
            <button 
              className="vote-btn upvote"
              onClick={() => handleVote('upvote')}
              disabled={!isAuthenticated}
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path d="M7 14l5-5 5 5" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
              {forum.upvotes || 0}
            </button>
            <button 
              className="vote-btn downvote"
              onClick={() => handleVote('downvote')}
              disabled={!isAuthenticated}
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path d="M17 10l-5 5-5-5" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
              {forum.downvotes || 0}
            </button>
          </div>
          <div className="comments-count">
            {forum.commentCount || 0} comments
          </div>
        </div>
      </div>

      <div className="comments-section">
        <h2>Discussion</h2>
        
        {isAuthenticated ? (
          <form onSubmit={handleCommentSubmit} className="comment-form">
            <textarea
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              placeholder="Share your thoughts..."
              rows={3}
              maxLength={500}
              required
            />
            <div className="comment-form-actions">
              <small>{newComment.length}/500 characters</small>
              <button 
                type="submit" 
                disabled={submittingComment || !newComment.trim()}
                className="submit-comment-btn"
              >
                {submittingComment ? 'Posting...' : 'Post Comment'}
              </button>
            </div>
          </form>
        ) : (
          <div className="auth-prompt">
            <p>Sign in to join the discussion</p>
          </div>
        )}

        <div className="comments-list">
          {commentsLoading ? (
            <div className="loading-spinner">Loading comments...</div>
          ) : comments.length === 0 ? (
            <div className="empty-comments">
              <p>No comments yet. Be the first to share your thoughts!</p>
            </div>
          ) : (
            comments.map((comment) => (
              <div key={comment.id} className="comment-card">
                <div className="comment-header">
                  <div className="comment-creator">
                    <div className="creator-avatar small">
                      {comment.userName ? comment.userName[0].toUpperCase() : '?'}
                    </div>
                    <div className="creator-info">
                      <span className="creator-name">{comment.userName || 'Anonymous'}</span>
                      <span className="post-time">{formatDate(comment.createdAt)}</span>
                    </div>
                  </div>
                </div>
                <div className="comment-content">
                  <p>{comment.content}</p>
                </div>
                <div className="comment-actions">
                  <div className="vote-section">
                    <button 
                      className="vote-btn upvote small"
                      onClick={() => voteOnComment(comment.id, user?.uid || '', 'upvote')}
                      disabled={!isAuthenticated}
                    >
                      <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
                        <path d="M7 14l5-5 5 5" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                      </svg>
                      {comment.upvotes || 0}
                    </button>
                    <button 
                      className="vote-btn downvote small"
                      onClick={() => voteOnComment(comment.id, user?.uid || '', 'downvote')}
                      disabled={!isAuthenticated}
                    >
                      <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
                        <path d="M17 10l-5 5-5-5" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                      </svg>
                      {comment.downvotes || 0}
                    </button>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}
