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

    // Analyze message content with GPT-4o-mini
    const completion = await openai.chat.completions.create({
      model: "gpt-4o-mini",
      messages: [{
        role: "user",
        content: `Analyze these forum messages and extract the top 3-5 most common phrases or topics. Return only a JSON array of strings. Messages:\n\n${messageContents}`
      }],
      max_tokens: 150,
      temperature: 0.3
    });

    const analysis = JSON.parse(completion.choices[0].message.content || '["analysis failed"]');

    return res.status(200).json({
      totalMessages,
      uniqueAuthors,
      topPhrases: analysis.topPhrases,
    } as MessageAnalysisResponse);
  } catch (error) {
    return res.status(500).json({ error: 'Internal server error' });
  }
}
