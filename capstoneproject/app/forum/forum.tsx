import type { User } from "firebase/auth";
import { useState } from "react";
import { Link } from "react-router";
import { useAuthState } from "../hooks/useAuthState";
import { useFirestore, useForums, type Forum } from "../hooks/useFirestore";
import "./forum.css";

export default function Forum() {
  const { user, isAuthenticated } = useAuthState();
  const { forums, loading } = useForums();

  if (loading) {
    return (
      <div className="forum-container">
        <div className="loading-spinner">Loading forums...</div>
      </div>
    );
  }

  return (
    <div className="forum-container">
      <div className="forum-header">
        <h1>Community Forums</h1>
        {isAuthenticated && (
          <Link to="/create-forum" className="create-forum-btn">
            + New Forum
          </Link>
        )}
      </div>

      {!isAuthenticated && (
        <div className="auth-prompt">
          <p>Sign in to create forums and participate in discussions!</p>
        </div>
      )}

      <div className="forums-feed">
        {forums.length === 0 ? (
          <div className="empty-state">
            <h2>No forums yet</h2>
            <p>Be the first to start a discussion!</p>
          </div>
        ) : (
          forums.map((forum) => (
            <ForumCard key={forum.id} forum={forum} currentUser={user} />
          ))
        )}
      </div>
    </div>
  );
}

interface ForumCardProps {
  forum: Forum;
  currentUser: User | null;
}

function ForumCard({ forum, currentUser }: ForumCardProps) {
  const { voteOnForum } = useFirestore();
  const [votingState, setVotingState] = useState<'upvote' | 'downvote' | null>(null);
  
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
      return 'Just now';
    }
  };

  const handleVote = async (voteType: 'upvote' | 'downvote') => {
    if (!currentUser || votingState || isExpired) return;
    
    setVotingState(voteType);
    try {
      await voteOnForum(forum.id, currentUser.uid, voteType);
    } catch (error) {
      console.error('Error voting:', error);
    } finally {
      setVotingState(null);
    }
  };

  // Simple time-based expiration only
  const isExpired = forum.endTime && forum.endTime.toDate() < new Date();
  const netScore = (forum.upvotes || 0) - (forum.downvotes || 0);

  return (
    <div className={`forum-card ${isExpired ? 'expired' : ''}`}>
      <div className="forum-header-info">
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

      <div className="forum-content">
        <h2 className="forum-title">
          {forum.title}
          {isExpired && <span className="expired-badge">CLOSED</span>}
        </h2>
        {forum.description && <p className="forum-description">{forum.description}</p>}
        <div className="forum-question">
          <strong>Question:</strong> {forum.question}
        </div>
        
        {/* Show time remaining for active forums */}
        {forum.endTime && !isExpired && (
          <div className="time-remaining">
            <small>Closes: {forum.endTime.toDate().toLocaleString()}</small>
          </div>
        )}
      </div>

      <div className="forum-actions">
        <div className="vote-section">
          <div className="net-score">
            <span className={`score ${netScore > 0 ? 'positive' : netScore < 0 ? 'negative' : 'neutral'}`}>
              {netScore > 0 ? '+' : ''}{netScore}
            </span>
          </div>
          <button 
            className="vote-btn upvote"
            onClick={() => handleVote('upvote')}
            disabled={!currentUser || isExpired || !!votingState}
            title={!currentUser ? "Sign in to vote" : isExpired ? "Forum is closed" : "Upvote"}
          >
            {votingState === 'upvote' ? (
              <div className="voting-spinner"></div>
            ) : (
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path d="M7 14l5-5 5 5" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            )}
            {forum.upvotes || 0}
          </button>
          <button 
            className="vote-btn downvote"
            onClick={() => handleVote('downvote')}
            disabled={!currentUser || isExpired || !!votingState}
            title={!currentUser ? "Sign in to vote" : isExpired ? "Forum is closed" : "Downvote"}
          >
            {votingState === 'downvote' ? (
              <div className="voting-spinner"></div>
            ) : (
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path d="M17 10l-5 5-5-5" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            )}
            {forum.downvotes || 0}
          </button>
        </div>

        <Link 
          to={isExpired ? "#" : `/forums/${forum.id}`} 
          className={`comments-link ${isExpired ? 'disabled' : ''}`}
          onClick={(e) => isExpired && e.preventDefault()}
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
          {forum.commentCount || 0} comments
        </Link>
      </div>
    </div>
  );
}