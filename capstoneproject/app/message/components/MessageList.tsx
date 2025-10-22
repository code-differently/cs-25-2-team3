/**
 * components/MessageList.tsx
 * Displays a list of messages with reactions and interactions.
 */

import React, { useEffect, useState } from 'react';
import { Message } from '../models/Message';
import type { MessageFilters } from '../services/MessageService';
import { MessageService } from '../services/MessageService';
import { AnalysisService, type MessageAnalysisResponse } from '../services/AnalysisService';
import { MessageItem } from './MessageItem';
import { MessageAnalysis } from './MessageAnalysis';

interface MessageListProps {
  filters?: MessageFilters;
  onMessageSelect?: (message: Message) => void;
  onMessagesLoaded?: (messages: Message[]) => void;
  onAnalysisComplete?: (analysis: MessageAnalysisResponse) => void;
  enableAnalysis?: boolean;
  className?: string;
}



export const MessageList: React.FC<MessageListProps> = ({
  filters,
  onMessageSelect,
  onMessagesLoaded,
  onAnalysisComplete,
  enableAnalysis = false,
  className = ''
}) => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [analyzing, setAnalyzing] = useState(false);
  const [analysisResults, setAnalysisResults] = useState<MessageAnalysisResponse | null>(null);

  const messageService = React.useMemo(() => new MessageService(), []);
  const analysisService = React.useMemo(() => new AnalysisService(), []);

  const analyzeMessages = React.useCallback(async (messagesToAnalyze: Message[]) => {
    if (!enableAnalysis || messagesToAnalyze.length === 0) return;
    
    try {
      setAnalyzing(true);
      const response = await fetch('/api/analyzeMessages', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ messages: messagesToAnalyze.map(msg => msg.toObject()) })
      });
      
      if (!response.ok) throw new Error('Analysis failed');
      
      const analysis: MessageAnalysisResponse = await response.json();
      onAnalysisComplete?.(analysis);
    } catch (err) {
      console.error('Message analysis failed:', err);
    } finally {
      setAnalyzing(false);
    }
  }, [enableAnalysis, onAnalysisComplete]);

  const loadMessages = React.useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const fetchedMessages = await messageService.getMessages(filters);
      setMessages(fetchedMessages);
      onMessagesLoaded?.(fetchedMessages);
      
      // Auto-analyze if enabled
      if (enableAnalysis && fetchedMessages.length > 0) {
        analyzeMessages(fetchedMessages);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load messages');
    } finally {
      setLoading(false);
    }
  }, [filters, messageService, onMessagesLoaded, enableAnalysis, analyzeMessages]);

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
    message.id && 
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
    <div className={`space-y-4 ${className}`}>
      {analyzing && enableAnalysis && (
        <div className="bg-blue-50 border border-blue-200 rounded-lg p-3 flex items-center">
          <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-blue-600"></div>
          <span className="ml-2 text-blue-700 text-sm">Analyzing messages with AI...</span>
        </div>
      )}
      
      <div role="main" aria-live="polite">
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
    </div>
  );
};
