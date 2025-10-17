/**
 * __tests__/services/ModerationService.test.ts
 * Jest tests for the ModerationService class
 */

import { ModerationService } from '../../services/ModerationService';
import type { ModerationResult } from '../../services/ModerationService';

// Mock global fetch
global.fetch = jest.fn();

describe('ModerationService', () => {
  let moderationService: ModerationService;
  const mockedFetch = global.fetch as jest.MockedFunction<typeof fetch>;
  
  // Mock data for testing
  const mockModerationResult: ModerationResult = {
    isApproved: true,
    flaggedWords: [],
    severity: 'low',
    suggestedAction: 'approve'
  };

  beforeEach(() => {
    moderationService = new ModerationService();
    mockedFetch.mockClear();
  });
});
