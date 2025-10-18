/**
 * components/MessageList.tsx
 * Displays a list of messages with reactions and interactions.
 */

import React, { useEffect, useState } from 'react';
import { Message } from '../models/Message';
import type { MessageFilters } from '../services/MessageService';
import { MessageService } from '../services/MessageService';
import { MessageItem } from './MessageItem';

interface MessageListProps {
  filters?: MessageFilters;
  onMessageSelect?: (message: Message) => void;
  className?: string;
}

export const MessageList: React.FC<MessageListProps> = ({
  filters,
  onMessageSelect,
  className = ''
}) => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const messageService = React.useMemo(() => new MessageService(), []);

  const loadMessages = React.useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const fetchedMessages = await messageService.getMessages(filters);
      setMessages(fetchedMessages);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load messages');
    } finally {
      setLoading(false);
    }
  }, [filters, messageService]);

  useEffect(() => {
    loadMessages();
  }, [loadMessages]);

  const handleMessageUpdate = (updatedMessage: Message) => {
    setMessages(prev => 
      prev.map(msg => msg.id === updatedMessage.id ? updatedMessage : msg)
    );
  };

  const handleMessageDelete = (messageId: number) => {
    setMessages(prev => prev.filter(msg => msg.id !== messageId));
  };

  if (loading) {
    return (
      <div className={`flex justify-center items-center p-8 ${className}`} role="status">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
        <span className="ml-2 text-gray-600">Loading messages...</span>
      </div>
    );
  }

  if (error) {
    return (
      <div className={`bg-red-50 border border-red-200 rounded-lg p-4 ${className}`}>
        <p className="text-red-600">Error: {error}</p>
        <button 
          onClick={loadMessages}
          className="mt-2 px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
        >
          Retry
        </button>
      </div>
    );
  }

  // Handle null/undefined messages
  if (!messages) {
    return (
      <div className={`text-center p-8 text-gray-500 ${className}`}>
        No messages yet
      </div>
    );
  }

  // Filter out invalid messages
  const validMessages = messages.filter(message => 
    message && 
    message.id !== undefined && message.id !== null &&
    message.content && 
    message.author
  );

  if (validMessages.length === 0) {
    return (
      <div className={`text-center p-8 text-gray-500 ${className}`}>
        No messages found.
      </div>
    );
  }

  return (
    <div className={`space-y-4 ${className}`} role="main" aria-live="polite">
      {validMessages.map(message => (
        <MessageItem
          key={message.id}
          message={message}
          onUpdate={handleMessageUpdate}
          onDelete={handleMessageDelete}
          onSelect={onMessageSelect}
        />
      ))}
    </div>
  );
};
