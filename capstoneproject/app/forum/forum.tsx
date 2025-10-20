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

