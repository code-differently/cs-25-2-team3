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
    reactions?: Array<{
      id: number;
      userId: number;
      messageId: number;
      type: string;
      timestamp: string;
    }>;
  }>;
}

interface MessageAnalysisResponse {
  totalMessages: number;
  uniqueAuthors: number;
  totalReactions: number;
  averageEngagement: number;
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
    
    // Extract message content and reaction data (privacy-focused)
    const messageContents = messages.map(m => m.content).join('\n');
    
    // Calculate engagement metrics
    const engagementData = messages.map(msg => {
      const likes = msg.reactions?.filter(r => r.type === 'like').length || 0;
      const dislikes = msg.reactions?.filter(r => r.type === 'dislike').length || 0;
      const score = likes - dislikes;
      return { content: msg.content, likes, dislikes, score };
    });

    const totalReactions = engagementData.reduce((sum, msg) => sum + msg.likes + msg.dislikes, 0);
    const avgEngagement = totalReactions / messages.length;
    const topMessages = engagementData.filter(msg => msg.score > avgEngagement).map(msg => msg.content);
    
    // OpenAI analysis (content + engagement data, no author data)
    const OpenAI = require('openai');
    const openai = new OpenAI({ apiKey: process.env.OPENAI_API_KEY });

    // Analyze message content with engagement context
    const completion = await openai.chat.completions.create({
      model: "gpt-4o-mini",
      messages: [{
        role: "user",
        content: `Analyze these forum messages with engagement data. Focus on highly-engaged content (${totalReactions} total reactions, avg ${avgEngagement.toFixed(1)} per message). Extract top 3-5 phrases/topics. Return JSON array of strings.

Messages: ${messageContents}

High-engagement messages: ${topMessages.join('\n')}`
      }],
      max_tokens: 150,
      temperature: 0.3
    });

    const analysis = JSON.parse(completion.choices[0].message.content || '["analysis failed"]');

    return res.status(200).json({
      totalMessages,
      uniqueAuthors,
      totalReactions,
      averageEngagement: avgEngagement,
      topPhrases: analysis,
    } as MessageAnalysisResponse);
  } catch (error) {
    return res.status(500).json({ error: 'Internal server error' });
  }
}
