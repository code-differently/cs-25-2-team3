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

  beforeEach(() => {
    messageService = new MessageService();
    mockedFetch.mockClear();
  });

  afterEach(() => {
    jest.resetAllMocks();
  });

  // ============ createMessage() Tests ============
  describe('createMessage()', () => {
    it('should create message and return Message instance on success', async () => {
      mockedFetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockMessageData
      } as Response);
    });
  });
});
