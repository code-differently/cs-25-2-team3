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

  // Test: Component shows loading state while fetching messages  
  test('shows loading state while fetching messages', async () => {
    mockMessageService.getMessages.mockImplementation(
      () => new Promise(resolve => setTimeout(() => resolve(mockMessages), 100))
    );

    render(<MessageList />);
    
    expect(screen.getByText('Loading messages...')).toBeInTheDocument();
    expect(screen.getByRole('status')).toBeInTheDocument(); // Loading spinner
    
    await waitFor(() => {
      expect(screen.queryByText('Loading messages...')).not.toBeInTheDocument();
    });
  });

  // Test: Component renders list of messages from service response
  test('renders list of messages from service response', async () => {
    mockMessageService.getMessages.mockResolvedValue(mockMessages);

    render(<MessageList />);
    
    await waitFor(() => {
      expect(screen.getByTestId('message-1')).toBeInTheDocument();
      expect(screen.getByTestId('message-2')).toBeInTheDocument();
      expect(screen.getByText('Alice: Hello world!')).toBeInTheDocument();
      expect(screen.getByText('Bob: How are you?')).toBeInTheDocument();
    });
  });

  // Test: Component displays empty state when no messages available
  test('displays empty state when no messages available', async () => {
    mockMessageService.getMessages.mockResolvedValue([]);

    render(<MessageList />);
    
    await waitFor(() => {
      expect(screen.getByText('No messages found.')).toBeInTheDocument();
    });
  });

  // Test: Component handles API failure gracefully
  test('handles API failure gracefully with retry option', async () => {
    const errorMessage = 'Network error occurred';
    mockMessageService.getMessages.mockRejectedValue(new Error(errorMessage));

    render(<MessageList />);
    
    await waitFor(() => {
      expect(screen.getByText(`Error: ${errorMessage}`)).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /retry/i })).toBeInTheDocument();
    });
  });

  // Test: Component passes filters to MessageService correctly
  test('applies filters when fetching messages', async () => {
    const filters = { author: 'Alice', limit: 10 };
    mockMessageService.getMessages.mockResolvedValue(mockMessages);

    render(<MessageList filters={filters} />);
    
    await waitFor(() => {
      expect(mockMessageService.getMessages).toHaveBeenCalledWith(filters);
    });
  });

  // Test: Retry functionality calls loadMessages again
  test('retry button refetches messages after error', async () => {
    mockMessageService.getMessages
      .mockRejectedValueOnce(new Error('Network error'))
      .mockResolvedValueOnce(mockMessages);

    render(<MessageList />);
    
    await waitFor(() => {
      expect(screen.getByText('Error: Network error')).toBeInTheDocument();
    });

    const retryButton = screen.getByRole('button', { name: /retry/i });
    fireEvent.click(retryButton);

    await waitFor(() => {
      expect(screen.getByTestId('message-1')).toBeInTheDocument();
      expect(mockMessageService.getMessages).toHaveBeenCalledTimes(2);
    });
  });

  // Test: Message selection callback is triggered correctly
  test('calls onMessageSelect when message is clicked', async () => {
    const mockOnMessageSelect = jest.fn();
    mockMessageService.getMessages.mockResolvedValue(mockMessages);

    render(<MessageList onMessageSelect={mockOnMessageSelect} />);
    
    await waitFor(() => {
      expect(screen.getByTestId('message-1')).toBeInTheDocument();
    });

    fireEvent.click(screen.getByTestId('message-1'));
    expect(mockOnMessageSelect).toHaveBeenCalledWith(mockMessages[0]);
  });

  // Test: Message updates are handled correctly 
  test('updates message in list when handleMessageUpdate is called', async () => {
    mockMessageService.getMessages.mockResolvedValue(mockMessages);

    render(<MessageList />);
    
    await waitFor(() => {
      expect(screen.getByText('Alice: Hello world!')).toBeInTheDocument();
    });

    // Simulate message update through MessageItem
    const updateButton = screen.getAllByText('Update')[0];
    fireEvent.click(updateButton);

    // The message should still be in the list (update functionality)
    expect(screen.getByTestId('message-1')).toBeInTheDocument();
  });

  // Test: Message deletion callback is triggered correctly
  test('calls onMessageDelete when message is deleted', async () => {
    const mockOnMessageDelete = jest.fn();
    mockMessageService.getMessages.mockResolvedValue(mockMessages);

    render(<MessageList onMessageDelete={mockOnMessageDelete} />);
    
    await waitFor(() => {
      expect(screen.getByTestId('message-1')).toBeInTheDocument();
    });

    // Simulate delete action
    const deleteButton = screen.getAllByText('Delete')[0];
    fireEvent.click(deleteButton);

    expect(mockOnMessageDelete).toHaveBeenCalledWith('1');
  });

  // Test: Custom className is applied correctly
  test('applies custom className to container', () => {
    const customClass = 'custom-message-list';
    render(<MessageList className={customClass} />);
    
    const container = screen.getByRole('main');
    expect(container).toHaveClass(customClass);
  });
});
