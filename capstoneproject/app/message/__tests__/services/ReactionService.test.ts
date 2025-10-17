/**
 * __tests__/services/ReactionService.test.ts
 * Jest tests for the ReactionService class
 */

import { ReactionService } from '../../services/ReactionService';
import type { AddReactionRequest } from '../../services/ReactionService';
import { Reaction, ReactionType } from '../../models/Reaction';
import type { ReactionData } from '../../models/Reaction';

// Mock global fetch
global.fetch = jest.fn();

describe('ReactionService', () => {
  let reactionService: ReactionService;
  const mockedFetch = global.fetch as jest.MockedFunction<typeof fetch>;
  
  // Mock data for testing
  const mockReactionData: ReactionData = {
    id: 1,
    userId: 123,
    messageId: 456,
    type: ReactionType.LIKE,
    timestamp: '2024-01-01T00:00:00Z'
  };

  beforeEach(() => {
    reactionService = new ReactionService();
    mockedFetch.mockClear();
  });

  afterEach(() => {
    jest.resetAllMocks();
  });
});
