/**
 * api/analyzeMessages.ts
 * Vercel serverless function that analyzes messages using OpenAI API
 * SECURE: Runs server-side, keeps API key safe
 */

import type { VercelRequest, VercelResponse } from '@vercel/node';

interface MessageAnalysisRequest {
  messages: Array<{
    id: number;
    author: string;
    content: string;
    timestamp: string;
  }>;
}
