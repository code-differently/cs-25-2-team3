/**
 * api/analyzeMessages.ts
 * POST handler that analyzes messages using OpenAI API
 */

import { ActionFunctionArgs, json } from "@react-router/node";
import OpenAI from "openai";

interface MessageAnalysisRequest {
  forumId: string;
  threadId?: string;
  forumTitle: string;
  forumDescription?: string;
  forumQuestion?: string;
  category?: string;
  messages: Array<{
    id: number;
    author: string;
    content: string;
    timestamp: string;
    threadId?: string;
  }>;
}

interface MessageAnalysisResponse {
  totalMessages: number;
  uniqueAuthors: number;
  summary: string;
  actionRoadmap: string[];
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

    const { messages, forumTitle, forumDescription, forumQuestion, category } = body;
    const totalMessages = messages.length;
    const uniqueAuthors = new Set(messages.map(m => m.author)).size;

    // Extract forum context from request body
    const forumContext = forumTitle 
      ? `Forum: "${forumTitle}"${forumDescription ? `\nDescription: ${forumDescription}` : ''}${forumQuestion ? `\nQuestion: ${forumQuestion}` : ''}${category ? `\nCategory: ${category}` : ''}\n\n` 
      : '';
    
    // Group messages by thread for hierarchical analysis
    const messagesByThread = new Map<string, typeof messages>();
    messages.forEach(msg => {
      const threadKey = msg.threadId || 'main';
      if (!messagesByThread.has(threadKey)) {
        messagesByThread.set(threadKey, []);
      }
      messagesByThread.get(threadKey)!.push(msg);
    });
    
    // Generate thread summaries first, then forum-level analysis
    const threadSummaries: string[] = [];
    for (const [threadKey, threadMessages] of messagesByThread) {
      const threadContent = threadMessages.map(m => m.content).join('\n');
      threadSummaries.push(`Thread "${threadKey}": ${threadContent}`);
    }
    
    const analysisPrompt = `
${forumContext}Perform hierarchical analysis:
1. Analyze each thread's diction, tone, structure, and emotional intent
2. Synthesize into forum-level summary with Gen Z-friendly insights
3. Create actionable 3-step roadmap

Threads:
${threadSummaries.join('\n\n')}

Return JSON: {"summary": "...", "actionRoadmap": ["1️⃣ ...", "2️⃣ ...", "3️⃣ ..."]}`;

    const completion = await openai.chat.completions.create({
      model: "gpt-4o-mini",
      messages: [{
        role: "user",
        content: analysisPrompt
      }],
      temperature: 0.3
    });

    const aiResponse = completion.choices[0]?.message?.content || "{}";
    let analysisResult: { summary: string; actionRoadmap: string[] } = { summary: "", actionRoadmap: [] };

    try {
      analysisResult = JSON.parse(aiResponse);
    } catch (parseError) {
      console.warn("Failed to parse OpenAI response, using fallback");
      analysisResult = {
        summary: "Messages show active discussion about tech topics with mixed engagement levels.",
        actionRoadmap: ["1️⃣ Start by identifying key themes", "2️⃣ Build on popular topics", "3️⃣ Finish with actionable insights"]
      };
    }

    const response: MessageAnalysisResponse = {
      totalMessages,
      uniqueAuthors,
      summary: analysisResult.summary,
      actionRoadmap: analysisResult.actionRoadmap
    };

    return json(response);
  } catch (error) {
    return json({ error: "Internal server error" }, { status: 500 });
  }
}
