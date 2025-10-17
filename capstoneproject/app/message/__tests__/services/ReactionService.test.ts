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
});
