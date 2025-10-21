/**
 * components/MessageItem.tsx
 * Displays an individual message with reactions and actions.
 */

import React, { useState } from 'react';
import { Message } from '../models/Message';
import { ReactionType } from '../models/Reaction';
import { ReactionService } from '../services/ReactionService';
import '../message.css';

interface MessageItemProps {
  message: Message;
  onUpdate?: (message: Message) => void;
  onDelete?: (messageId: number) => void;
  onSelect?: (message: Message) => void;
  className?: string;
}

export const MessageItem: React.FC<MessageItemProps> = ({
  message,
  onUpdate,
  onDelete,
  onSelect,
  className = ''
}) => {
  const [isEditing, setIsEditing] = useState(false);
  const [editContent, setEditContent] = useState(message.content);
  const [reactionCounts, setReactionCounts] = useState<Record<ReactionType, number>>({} as Record<ReactionType, number>);

  const reactionService = new ReactionService();

  const handleEdit = () => {
    setIsEditing(true);
    setEditContent(message.content);
  };

  const handleSaveEdit = async () => {
    // Placeholder - would call MessageService.updateMessage
    try {
      // const updatedMessage = await messageService.updateMessage(message.id, editContent);
      // onUpdate?.(updatedMessage);
      setIsEditing(false);
    } catch (error) {
      console.error('Failed to update message:', error);
    }
  };

  const handleCancelEdit = () => {
    setIsEditing(false);
    setEditContent(message.content);
  };

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this message?')) {
      onDelete?.(message.id);
    }
  };

  const handleReaction = async (type: ReactionType) => {
    try {
      // Placeholder - would get current user ID from context/auth
      const userId = 1; // This should come from auth context
      await reactionService.addReaction({
        messageId: message.id,
        userId,
        type
      });
      // Refresh reaction counts
      const counts = await reactionService.getReactionCounts(message.id);
      setReactionCounts(counts);
    } catch (error) {
      console.error('Failed to add reaction:', error);
    }
  };

  const formatTime = (timestamp: string): string => {
    return new Date(timestamp).toLocaleString();
  };

  const reactionEmojis: Record<ReactionType, string> = {
    [ReactionType.LIKE]: '👍',
    [ReactionType.DISLIKE]: '👎'
  };

  return (
    <div 
      className={`message-card ${className}`}
      onClick={() => onSelect?.(message)}
    >
      {/* Message Header */}
      <div className="message-header">
        <div className="message-meta">
          <span className="message-author">{message.author}</span>
          <span className="message-timestamp">{formatTime(message.timestamp)}</span>
        </div>
        
        <div className="flex space-x-2">
          <button
            onClick={(e) => { e.stopPropagation(); handleEdit(); }}
            className="text-gray-400 hover:text-blue-600 text-sm"
          >
            Edit
          </button>
          <button
            onClick={(e) => { e.stopPropagation(); handleDelete(); }}
            className="text-gray-400 hover:text-red-600 text-sm"
          >
            Delete
          </button>
        </div>
      </div>

      {/* Message Content */}
      <div className="mb-3">
        {isEditing ? (
          <div className="space-y-2">
            <textarea
              value={editContent}
              onChange={(e) => setEditContent(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded resize-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              rows={3}
            />
            <div className="flex space-x-2">
              <button
                onClick={handleSaveEdit}
                className="px-3 py-1 bg-blue-600 text-white text-sm rounded hover:bg-blue-700"
              >
                Save
              </button>
              <button
                onClick={handleCancelEdit}
                className="px-3 py-1 bg-gray-300 text-gray-700 text-sm rounded hover:bg-gray-400"
              >
                Cancel
              </button>
            </div>
          </div>
        ) : (
          <p className="text-gray-800 whitespace-pre-wrap">{message.content}</p>
        )}
      </div>

      {/* Reactions */}
      <div className="flex items-center space-x-4">
        <div className="flex space-x-2">
          {Object.values(ReactionType).map(type => (
            <button
              key={type}
              onClick={(e) => { e.stopPropagation(); handleReaction(type); }}
              className="flex items-center space-x-1 px-2 py-1 rounded-full bg-gray-100 hover:bg-gray-200 transition-colors"
            >
              <span>{reactionEmojis[type]}</span>
              <span className="text-sm text-gray-600">{reactionCounts[type] || 0}</span>
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};
