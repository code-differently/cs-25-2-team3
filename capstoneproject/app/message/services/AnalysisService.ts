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
