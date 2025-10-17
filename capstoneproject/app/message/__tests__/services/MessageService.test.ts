/**
 * __tests__/services/MessageService.test.ts
 * Jest tests for the MessageService class
 */

import { MessageService } from '../../services/MessageService';
import type { CreateMessageRequest, MessageFilters } from '../../services/MessageService';
import { Message } from '../../models/Message';
import type { MessageData } from '../../models/Message';

// Mock global fetch
global.fetch = jest.fn();

describe('MessageService', () => {
  let messageService: MessageService;
  const mockedFetch = global.fetch as jest.MockedFunction<typeof fetch>;
  
  // Mock data for testing
  const mockMessageData: MessageData = {
    id: 1,
    author: 'testuser',
    content: 'Test message content',
    timestamp: '2024-01-01T00:00:00Z',
    reactions: []
  };

  const mockCreateRequest: CreateMessageRequest = {
    author: 'testuser',
    content: 'Test message content'
  };

  beforeEach(() => {
    messageService = new MessageService();
    mockedFetch.mockClear();
  });

  afterEach(() => {
    jest.resetAllMocks();
  });

  // ============ createMessage() Tests ============
  describe('createMessage()', () => {
    // Success case: creates message and returns Message instance
    it('should create message and return Message instance on success', async () => {
      mockedFetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockMessageData
      } as Response);

      const result = await messageService.createMessage(mockCreateRequest);

      expect(mockedFetch).toHaveBeenCalledWith('/api/messages', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(mockCreateRequest)
      });
      expect(result).toBeInstanceOf(Message);
      expect(result.id).toBe(1);
      expect(result.author).toBe('testuser');
    });
    // ✅ Commit: Add success test for createMessage()

    // Error case: throws error when response is not ok
    it('should throw error when create message fails', async () => {
      mockedFetch.mockResolvedValueOnce({
        ok: false,
        status: 400
      } as Response);

      await expect(messageService.createMessage(mockCreateRequest))
        .rejects.toThrow('Failed to create message');
      
      expect(mockedFetch).toHaveBeenCalledTimes(1);
    });
    // ✅ Commit: Add error handling test for createMessage()
  });

  // ============ getMessages() Tests ============
  describe('getMessages()', () => {
    // Success case: fetches messages without filters
    it('should fetch messages without filters and return Message array', async () => {
      const mockResponseData = [mockMessageData];
      mockedFetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockResponseData
      } as Response);

      const result = await messageService.getMessages();

      expect(mockedFetch).toHaveBeenCalledWith('/api/messages');
      expect(result).toHaveLength(1);
      expect(result[0]).toBeInstanceOf(Message);
      expect(result[0].id).toBe(1);
    });
    // ✅ Commit: Add success test for getMessages() without filters
  });
});
