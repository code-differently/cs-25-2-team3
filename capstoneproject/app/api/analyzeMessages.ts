/**
 * api/analyzeMessages.ts
 * POST handler that analyzes messages using OpenAI API
 */

import { ActionFunctionArgs, json } from "@react-router/node";
import OpenAI from "openai";

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

// OpenAI client setup
const OPENAI_API_KEY = process.env.OPENAI_API_KEY;

export async function action({ request }: ActionFunctionArgs): Promise<Response> {
  if (request.method !== "POST") {
    return json({ error: "Method not allowed" }, { status: 405 });
  }

  try {
    const body: MessageAnalysisRequest = await request.json();
    
    if (!OPENAI_API_KEY) {
      return json({ error: "OpenAI API key not configured" }, { status: 500 });
    }

    const openai = new OpenAI({ 
      apiKey: OPENAI_API_KEY 
    });

    const { messages } = body;
    const totalMessages = messages.length;
    const uniqueAuthors = new Set(messages.map(m => m.author)).size;

    // Prepare content for OpenAI analysis
    const messageContents = messages.map(m => m.content).join('\n');
    
    const completion = await openai.chat.completions.create({
      model: "gpt-4o-mini",
      messages: [{
        role: "user",
        content: `Analyze these messages and extract the top 5 most common phrases or themes. Return only a JSON array of strings: ${messageContents}`
      }],
      temperature: 0.3
    });

    const aiResponse = completion.choices[0]?.message?.content || "[]";
    let topPhrases: string[] = [];

    try {
      topPhrases = JSON.parse(aiResponse);
    } catch (parseError) {
      console.warn("Failed to parse OpenAI response, using fallback");
      topPhrases = ["analysis", "messages", "communication"];
    }

    const response: MessageAnalysisResponse = {
      totalMessages,
      uniqueAuthors,
      topPhrases
    };

    return json(response);
  } catch (error) {
    return json({ error: "Internal server error" }, { status: 500 });
  }
}
