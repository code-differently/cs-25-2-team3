/**
 * components/TeaModal.tsx
 * Modal popup for displaying AI analysis results in a chat-like interface
 */

import React from 'react';

interface TeaModalProps {
  onClose: () => void;
}

export default function TeaModal({ onClose }: TeaModalProps) {
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
