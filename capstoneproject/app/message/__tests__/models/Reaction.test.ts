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

  // Constructor initializes all fields correctly
  it('should initialize all fields correctly via constructor', () => {
    const reaction = new Reaction(mockReactionData);
    
    expect(reaction.id).toBe(1);
    expect(reaction.userId).toBe(123);
    expect(reaction.messageId).toBe(456);
    expect(reaction.type).toBe(ReactionType.LIKE);
    expect(reaction.timestamp).toBe('2024-01-01T00:00:00Z');
  });
});
