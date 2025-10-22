/**
 * services/AnalysisService.ts
 * Handles frontend calls to secure OpenAI analysis API
 */

export interface MessageAnalysisResponse {
  totalMessages: number;
  topPhrases: string[];
}

export class AnalysisService {
  /**
   * Analyzes messages using the secure OpenAI API endpoint
   */
  async analyzeMessages(messages: any[]): Promise<MessageAnalysisResponse> {
    const response = await fetch('/api/analyzeMessages', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ messages }),
    });

    if (!response.ok) {
      throw new Error('Failed to analyze messages');
    }

    return await response.json();
  }
}
