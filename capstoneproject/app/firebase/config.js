/**
 * Centralized Firebase configuration
 * This ensures consistent configuration across all Firebase usage
 */

import { getApp, getApps, initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from "firebase/firestore";

// Use environment variables first, fall back to hardcoded config for development
const firebaseConfig = {
  apiKey: import.meta.env.VITE_FIREBASE_API_KEY || "AIzaSyAQoQj4l2dqSrKX8jt12SALq_TEHbC9C54",
  authDomain: import.meta.env.VITE_FIREBASE_AUTH_DOMAIN || "codesocietycapstone.firebaseapp.com",
  projectId: import.meta.env.VITE_FIREBASE_PROJECT_ID || "codesocietycapstone",
  storageBucket: import.meta.env.VITE_FIREBASE_STORAGE_BUCKET || "codesocietycapstone.firebasestorage.app",
  messagingSenderId: import.meta.env.VITE_FIREBASE_MESSAGING_SENDER_ID || "659882615069",
  appId: import.meta.env.VITE_FIREBASE_APP_ID || "1:659882615069:web:a53a129aac0763130541f4",
  measurementId: import.meta.env.VITE_FIREBASE_MEASUREMENT_ID || "G-6XN9Z2H7WN"
};

// Temporary verification log (will remove after verification)
console.log("🔥 Firebase Frontend Config Verification:");
console.log("- VITE_FIREBASE_API_KEY available:", !!import.meta.env.VITE_FIREBASE_API_KEY);
console.log("- Using API Key:", import.meta.env.VITE_FIREBASE_API_KEY ? "ENV variable" : "fallback");

// Initialize Firebase with singleton pattern
const app = !getApps().length ? initializeApp(firebaseConfig) : getApp();

// Export Firebase services
export const firebaseAuth = getAuth(app);
export const db = getFirestore(app);
export default app;
