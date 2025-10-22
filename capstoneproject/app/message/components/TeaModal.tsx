/**
 * components/TeaModal.tsx
 * Modal popup for displaying AI analysis results in a chat-like interface
 */

import React, { useState, useEffect } from 'react';
import { AnalysisService, type MessageAnalysisResponse } from '../services/AnalysisService';
import type { Message } from '../models/Message';

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
        const result = await analysisService.analyzeMessages(messages);
        setAnalysis(result);
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
        
        {/* Body Placeholder */}
        <p className="text-gray-600">Fetching summary and solutions...</p>
      </div>
    </div>
  );
}
