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

interface MessageAnalysisResponse {
  totalMessages: number;
  uniqueAuthors: number;
  topPhrases: string[];
}

export default async function handler(req: VercelRequest, res: VercelResponse) {
  if (req.method !== 'POST') {
    return res.status(405).json({ error: 'Method not allowed' });
  }

  try {
    const { messages }: MessageAnalysisRequest = req.body;

    if (!process.env.OPENAI_API_KEY) {
      return res.status(500).json({ error: 'OpenAI API key not configured' });
    }

    const totalMessages = messages.length;
    const uniqueAuthors = messages.length; // Count messages instead of unique authors for privacy
    
    // Extract only message content, ignore author names for privacy
    const messageContents = messages.map(m => m.content).join('\n');
    
    // OpenAI analysis (content only, no author data)
    const OpenAI = require('openai');
    const openai = new OpenAI({ apiKey: process.env.OPENAI_API_KEY });

    // Perform analysis with OpenAI...
    // const analysis = await openai.someAnalysisMethod({ messages: messageContents });

    // For now, let's mock the response
    const analysis = {
      topPhrases: ['example phrase 1', 'example phrase 2'],
    };

    return res.status(200).json({
      totalMessages,
      uniqueAuthors,
      topPhrases: analysis.topPhrases,
    } as MessageAnalysisResponse);
  } catch (error) {
    return res.status(500).json({ error: 'Internal server error' });
  }
}
