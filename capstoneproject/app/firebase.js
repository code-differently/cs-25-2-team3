// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAuth } from 'firebase/auth';

// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyAQoQj4l2dqSrKX8jt12SALq_TEHbC9C54",
  authDomain: "codesocietycapstone.firebaseapp.com",
  projectId: "codesocietycapstone",
  storageBucket: "codesocietycapstone.firebasestorage.app",
  messagingSenderId: "659882615069",
  appId: "1:659882615069:web:a53a129aac0763130541f4",
  measurementId: "G-6XN9Z2H7WN"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export const firebaseAuth = getAuth(app);

export default app;