import { Link } from "react-router";
import { useAuthState } from "../hooks/useAuthState";
import { useForums } from "../hooks/useFirestore";
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
  forum: any; // We'll properly type this
  currentUser: any;
}

function ForumCard({ forum, currentUser }: ForumCardProps) {
  const formatDate = (timestamp: any) => {
    if (!timestamp) return '';
    const date = timestamp.toDate ? timestamp.toDate() : new Date(timestamp);
    return new Intl.RelativeTimeFormat('en', { numeric: 'auto' }).format(
      Math.floor((date.getTime() - Date.now()) / (1000 * 60 * 60 * 24)),
      'day'
    );
  };

  return (
    <div className="forum-card">
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
        <h2 className="forum-title">{forum.title}</h2>
        <p className="forum-description">{forum.description}</p>
        <div className="forum-question">
          <strong>Question:</strong> {forum.question}
        </div>
      </div>

      <div className="forum-actions">
        <div className="vote-section">
          <button className="vote-btn upvote">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
              <path d="M7 14l5-5 5 5" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
            </svg>
            {forum.upvotes || 0}
          </button>
          <button className="vote-btn downvote">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
              <path d="M17 10l-5 5-5-5" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
            </svg>
            {forum.downvotes || 0}
          </button>
        </div>

        <Link to={`/forums/${forum.id}`} className="comments-link">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
          {forum.commentCount || 0} comments
        </Link>
      </div>
    </div>
  );
}