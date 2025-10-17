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

  // ============ addReaction() Tests ============
  describe('addReaction()', () => {
    it('should add reaction and return Reaction instance on success', async () => {
      const mockAddRequest: AddReactionRequest = {
        messageId: 456,
        userId: 123,
        type: ReactionType.LIKE
      };

      mockedFetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockReactionData
      } as Response);

      const result = await reactionService.addReaction(mockAddRequest);

      expect(mockedFetch).toHaveBeenCalledWith('/api/reactions', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(mockAddRequest)
      });
      expect(result).toBeInstanceOf(Reaction);
      expect(result.id).toBe(1);
      expect(result.type).toBe(ReactionType.LIKE);
    });
  });
});
