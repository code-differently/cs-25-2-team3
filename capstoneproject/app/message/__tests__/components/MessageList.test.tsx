/**
 * __tests__/components/MessageList.test.tsx
 * Unit tests for MessageList component with ~80% coverage
 */

import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import { MessageList } from '../../components/MessageList';
import { Message } from '../../models/Message';
import { MessageService } from '../../services/MessageService';

// Mock the MessageService to avoid API calls
jest.mock('../../services/MessageService');
// Mock MessageItem component to focus on MessageList behavior
jest.mock('../../components/MessageItem', () => ({
  MessageItem: ({ message, onUpdate, onDelete, onSelect }: any) => (
    <div data-testid={`message-${message.id}`} onClick={() => onSelect?.(message)}>
      <span>{message.author}: {message.content}</span>
      <button onClick={() => onUpdate(message)}>Update</button>
      <button onClick={() => onDelete(message.id)}>Delete</button>
    </div>
  )
}));

const MockedMessageService = MessageService as jest.MockedClass<typeof MessageService>;

describe('MessageList', () => {
  let mockMessageService: jest.Mocked<MessageService>;
  let mockMessages: Message[];

  beforeEach(() => {
    jest.clearAllMocks();
    
    // Setup message service mock
    mockMessageService = {
      getMessages: jest.fn(),
    } as any;
    
    MockedMessageService.mockImplementation(() => mockMessageService);

    // Create mock messages
    mockMessages = [
      new Message({
        id: 1,
        author: 'Alice',
        content: 'Hello world!',
        timestamp: '2025-01-01T12:00:00Z'
      }),
      new Message({
        id: 2,
        author: 'Bob', 
        content: 'How are you?',
        timestamp: '2025-01-01T12:05:00Z'
      })
    ];
  });
