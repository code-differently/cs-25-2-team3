/**
 * __tests__/components/MessageComposer.test.tsx
 * Unit tests for MessageComposer component with ~80% coverage
 */

import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import '@testing-library/jest-dom';
import { MessageComposer } from '../../components/MessageComposer';
import { MessageService } from '../../services/MessageService';
import { ModerationService } from '../../services/ModerationService';

// Mock the services to avoid network calls
jest.mock('../../services/MessageService');
jest.mock('../../services/ModerationService');

const MockedMessageService = MessageService as jest.MockedClass<typeof MessageService>;
const MockedModerationService = ModerationService as jest.MockedClass<typeof ModerationService>;

describe('MessageComposer', () => {
  let mockMessageService: jest.Mocked<MessageService>;
  let mockModerationService: jest.Mocked<ModerationService>;
  let mockOnMessageCreated: jest.Mock;

  beforeEach(() => {
    // Reset mocks before each test
    jest.clearAllMocks();
    
    // Setup service mocks
    mockMessageService = {
      createMessage: jest.fn(),
    } as any;
    
    mockModerationService = {
      quickValidation: jest.fn(),
      checkContent: jest.fn(),
    } as any;

    MockedMessageService.mockImplementation(() => mockMessageService);
    MockedModerationService.mockImplementation(() => mockModerationService);

    mockOnMessageCreated = jest.fn();
  });

  // Test: Component renders with required form elements
  test('renders author and content fields with submit button', () => {
    render(<MessageComposer />);
    
    expect(screen.getByLabelText(/your name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/message/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /post message/i })).toBeInTheDocument();
    expect(screen.getByText(/0\/5000 characters/i)).toBeInTheDocument();
  });

  // Test: Custom placeholder prop is applied
  test('displays custom placeholder when provided', () => {
    const customPlaceholder = 'Share your thoughts...';
    render(<MessageComposer placeholder={customPlaceholder} />);
    
    expect(screen.getByPlaceholderText(customPlaceholder)).toBeInTheDocument();
  });

  // Test: Validation error when fields are empty (corresponds to sequence diagram validation step)
  test('shows validation error when submitting empty fields', async () => {
    render(<MessageComposer />);
    
    const submitButton = screen.getByRole('button', { name: /post message/i });
    fireEvent.click(submitButton);

    expect(screen.getByText('Please fill in all fields')).toBeInTheDocument();
    expect(mockModerationService.quickValidation).not.toHaveBeenCalled();
  });

  // Test: Quick validation failure blocks submission
  test('shows error when quick validation fails', async () => {
    mockModerationService.quickValidation.mockReturnValue(false);
    
    render(<MessageComposer />);
    
    await userEvent.type(screen.getByLabelText(/your name/i), 'John Doe');
    await userEvent.type(screen.getByLabelText(/message/i), 'Invalid content');
    
    fireEvent.click(screen.getByRole('button', { name: /post message/i }));

    expect(screen.getByText('Message content is invalid')).toBeInTheDocument();
    expect(mockModerationService.quickValidation).toHaveBeenCalledWith('Invalid content');
    expect(mockModerationService.checkContent).not.toHaveBeenCalled();
  });

  // Test: Moderation rejection (corresponds to moderation step in sequence diagram)
  test('handles moderation rejection with appropriate error message', async () => {
    mockModerationService.quickValidation.mockReturnValue(true);
    mockModerationService.checkContent.mockResolvedValue({
      isApproved: false,
      flaggedWords: ['bad'],
      severity: 'high' as const,
      suggestedAction: 'review' as const
    });

    render(<MessageComposer />);
    
    await userEvent.type(screen.getByLabelText(/your name/i), 'John Doe');
    await userEvent.type(screen.getByLabelText(/message/i), 'Bad content here');
    
    fireEvent.click(screen.getByRole('button', { name: /post message/i }));

    await waitFor(() => {
      expect(screen.getByText('Message rejected: review')).toBeInTheDocument();
    });
    
    expect(mockModerationService.checkContent).toHaveBeenCalledWith('Bad content here');
    expect(mockMessageService.createMessage).not.toHaveBeenCalled();
  });

  // Test: Successful message creation (corresponds to API call and acknowledgment in sequence diagram)
  test('creates message successfully and calls onMessageCreated', async () => {
    const mockCreatedMessage = {
      id: 1,
      author: 'John Doe',
      content: 'Hello world',
      timestamp: '2025-01-01T00:00:00Z'
    };

    mockModerationService.quickValidation.mockReturnValue(true);
    mockModerationService.checkContent.mockResolvedValue({
      isApproved: true,
      flaggedWords: [],
      severity: 'low' as const,
      suggestedAction: 'approve' as const
    });
    mockMessageService.createMessage.mockResolvedValue(mockCreatedMessage as any);

    render(<MessageComposer onMessageCreated={mockOnMessageCreated} />);
    
    const authorInput = screen.getByLabelText(/your name/i);
    const contentInput = screen.getByLabelText(/message/i);
    
    await userEvent.type(authorInput, 'John Doe');
    await userEvent.type(contentInput, 'Hello world');
    
    fireEvent.click(screen.getByRole('button', { name: /post message/i }));

    await waitFor(() => {
      expect(mockOnMessageCreated).toHaveBeenCalledWith(mockCreatedMessage);
    });

    // Form should be reset after successful submission
    expect(authorInput).toHaveValue('');
    expect(contentInput).toHaveValue('');
    expect(screen.getByText(/0\/5000 characters/i)).toBeInTheDocument();
  });

  // Test: API error handling
  test('displays API error when message creation fails', async () => {
    mockModerationService.quickValidation.mockReturnValue(true);
    mockModerationService.checkContent.mockResolvedValue({
      isApproved: true,
      flaggedWords: [],
      severity: 'low' as const,
      suggestedAction: 'approve' as const
    });
    mockMessageService.createMessage.mockRejectedValue(new Error('Network error'));

    render(<MessageComposer />);
    
    await userEvent.type(screen.getByLabelText(/your name/i), 'John Doe');
    await userEvent.type(screen.getByLabelText(/message/i), 'Hello world');
    
    fireEvent.click(screen.getByRole('button', { name: /post message/i }));

    await waitFor(() => {
      expect(screen.getByText('Network error')).toBeInTheDocument();
    });
  });

  // Test: Form state during submission (loading state)
  test('disables form and shows loading state while submitting', async () => {
    mockModerationService.quickValidation.mockReturnValue(true);
    mockModerationService.checkContent.mockImplementation(
      () => new Promise(resolve => setTimeout(() => resolve({
        isApproved: true,
        flaggedWords: [],
        severity: 'low' as const,
        suggestedAction: 'approve' as const
      }), 100))
    );

    render(<MessageComposer />);
    
    await userEvent.type(screen.getByLabelText(/your name/i), 'John Doe');
    await userEvent.type(screen.getByLabelText(/message/i), 'Hello world');
    
    fireEvent.click(screen.getByRole('button', { name: /post message/i }));

    // Should show loading state
    expect(screen.getByText('Posting...')).toBeInTheDocument();
    expect(screen.getByLabelText(/your name/i)).toBeDisabled();
    expect(screen.getByLabelText(/message/i)).toBeDisabled();
    expect(screen.getByRole('button', { name: /posting.../i })).toBeDisabled();

    // Wait for submission to complete
    await waitFor(() => {
      expect(screen.getByText('Post Message')).toBeInTheDocument();
    }, { timeout: 200 });
  });

  // Test: Error clearing behavior
  test('clears error when user starts typing after validation error', async () => {
    render(<MessageComposer />);
    
    // Trigger validation error
    fireEvent.click(screen.getByRole('button', { name: /post message/i }));
    expect(screen.getByText('Please fill in all fields')).toBeInTheDocument();

    // Start typing in author field
    await userEvent.type(screen.getByLabelText(/your name/i), 'J');
    expect(screen.queryByText('Please fill in all fields')).not.toBeInTheDocument();
  });

  // Test: Character count updates
  test('updates character count as user types', async () => {
    render(<MessageComposer />);
    
    const contentInput = screen.getByLabelText(/message/i);
    await userEvent.type(contentInput, 'Hello');
    
    expect(screen.getByText(/5\/5000 characters/i)).toBeInTheDocument();
  });

  // Test: Button state management based on form validity
  test('enables submit button only when both fields have content', async () => {
    render(<MessageComposer />);
    
    const submitButton = screen.getByRole('button', { name: /post message/i });
    const authorInput = screen.getByLabelText(/your name/i);
    const contentInput = screen.getByLabelText(/message/i);
    
    // Initially disabled
    expect(submitButton).toBeDisabled();
    
    // Still disabled with only author
    await userEvent.type(authorInput, 'John');
    expect(submitButton).toBeDisabled();
    
    // Enabled when both fields have content
    await userEvent.type(contentInput, 'Hello');
    expect(submitButton).toBeEnabled();
    
    // Disabled again when content is cleared
    await userEvent.clear(contentInput);
    expect(submitButton).toBeDisabled();
  });
});
