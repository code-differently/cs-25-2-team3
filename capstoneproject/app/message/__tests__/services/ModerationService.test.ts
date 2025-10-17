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

  afterEach(() => {
    jest.resetAllMocks();
  });

  // ============ checkContent() Tests ============
  describe('checkContent()', () => {
    it('should check content and return moderation result on success', async () => {
      mockedFetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockModerationResult
      } as Response);

      const result = await moderationService.checkContent('Test message');

      expect(mockedFetch).toHaveBeenCalledWith('/api/moderation/check', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ content: 'Test message' })
      });
      expect(result).toEqual(mockModerationResult);
      expect(result.isApproved).toBe(true);
    });
  });
});
