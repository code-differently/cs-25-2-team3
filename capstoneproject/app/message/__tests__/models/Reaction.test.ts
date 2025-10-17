/**
 * __tests__/models/Reaction.test.ts
 * Jest tests for the Reaction model
 */

import { Reaction, ReactionData, ReactionType } from '../../models/Reaction';

describe('Reaction Model', () => {
  // Test data setup
  const mockReactionData: ReactionData = {
    id: 1,
    userId: 123,
    messageId: 456,
    type: ReactionType.LIKE,
    timestamp: '2024-01-01T00:00:00Z'
  };
});
