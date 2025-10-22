/**
 * components/TeaModal.tsx
 * Modal popup for displaying AI analysis results in a chat-like interface
 */

import { useEffect, useState } from 'react';
import type { Message } from '../models/Message';
import { AnalysisService, type MessageAnalysisResponse } from '../services/AnalysisService';

interface TeaModalProps {
  onClose: () => void;
  messages: Message[];
}

export default function TeaModal({ onClose, messages }: TeaModalProps) {
  const [analysis, setAnalysis] = useState<MessageAnalysisResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const analysisService = new AnalysisService();

  useEffect(() => {
    const fetchAnalysis = async () => {
      try {
        // TODO: Remove - Mock data for testing
        await new Promise(resolve => setTimeout(resolve, 2000)); // Simulate API delay
        const mockResult: MessageAnalysisResponse = {
          totalMessages: messages.length,
          uniqueAuthors: 3,
          summary: "This thread is giving major productivity vibes! People are sharing solid React tips and debugging tricks. The tone is helpful and collaborative - perfect energy! 💯",
          actionRoadmap: [
            "1️⃣ Save the most upvoted tips to your notes",
            "2️⃣ Try implementing one new debugging technique",
            "3️⃣ Share your own experience to keep the convo flowing"
          ]
        };
        setAnalysis(mockResult);
        // Real API call: const result = await analysisService.analyzeMessages(messages);
      } catch (error) {
        console.error('Analysis failed:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchAnalysis();
  }, [messages]);

  return (
    <div className="fixed inset-0 bg-blue-900 bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-lg max-w-md w-full mx-4 p-6 relative">
        {/* Header */}
        <h2 className="text-xl font-semibold text-gray-900 mb-4">What's Tea?</h2>
        
        {/* Close Button */}
        <button onClick={onClose} className="absolute top-4 right-4 text-gray-500 hover:text-gray-700">
          ✕
        </button>
        
        {/* Body Content */}
        <div className="space-y-3">
          {loading ? (
            <div className="flex items-center space-x-2">
              <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-blue-600"></div>
              <p className="text-gray-600">Fetching the tea...</p>
            </div>
          ) : analysis ? (
            <div className="space-y-4">
              <p className="text-sm text-gray-500">{analysis.totalMessages} messages • {analysis.uniqueAuthors} authors</p>
              
              {/* Summary */}
              <div className="bg-blue-50 p-3 rounded-lg">
                <h3 className="font-medium text-blue-900 mb-2">The Tea ☕</h3>
                <p className="text-blue-800">{analysis.summary}</p>
              </div>
              
              {/* Action Roadmap */}
              <div className="bg-green-50 p-3 rounded-lg">
                <h3 className="font-medium text-green-900 mb-2">Action Plan 🚀</h3>
                <div className="space-y-1">
                  {analysis.actionRoadmap.map((step, index) => (
                    <p key={index} className="text-green-800 text-sm">{step}</p>
                  ))}
                </div>
              </div>
            </div>
          ) : (
            <p className="text-red-600">Unable to spill the tea right now 🫖</p>
          )}
        </div>
      </div>
    </div>
  );
}
