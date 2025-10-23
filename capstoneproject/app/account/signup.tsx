import { createUserWithEmailAndPassword } from 'firebase/auth';
import React, { useState } from 'react';
import { Footer } from '~/components/footer/footer';
import { firebaseAuth } from "../firebase";

export const SignUpPage: React.FC = () => {
  const [displayName, setDisplayName] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [error, setError] = useState<string | null>(null);

  const handleSignUp = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    try {
      await createUserWithEmailAndPassword(firebaseAuth, email, password);
      alert('User created successfully!');
      // Optionally redirect user to another page, e.g., login or dashboard
      // window.location.replace('/login'); 
    } catch (err: any) {
      setError(err.message);
      console.error('Sign-up error:', err);
    }
  };

  return (
    <div className="min-h-screen bg-white flex-col">
      <div className="grid grid-cols-2 bg-[#f4f4f4]">
        <div>
          <img src="./DevTalkLogoSmallTanBG.png" alt="DevTalk Logo" className="h-full w-full object-cover p-16" />
        </div>  
        <div className="h-full m-16">
          <h2 className="text-8xl text-gray-800 font-bold mb-12 underline decoration-[#F47D26]"><em>Sign Up</em></h2>
          <form onSubmit={handleSignUp}>
            <div className="w-2/3">
              <label className="text-2xl text-gray-800 mt-2" htmlFor="displayName">Username:</label>
              <input
                type="text"
                className="w-full border-gray-800 rounded px-3 py-2 border border-gray-300 my-4"
                id="displayName"
                value={displayName}
                onChange={(e) => setDisplayName(e.target.value)}
                required
              />
            </div>
            <div className="w-2/3">
              <label className="text-2xl text-gray-800 mt-2" htmlFor="email">Email:</label>
              <input
                type="email"
                id="email"
                className="w-full border-gray-800 rounded px-3 py-2 border border-gray-300 my-4"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <div className="w-2/3">
              <label className="text-2xl text-gray-800 mt-2" htmlFor="password">Password:</label>
              <input
                type="password"
                id="password"
                className="w-full border-gray-800  rounded px-3 py-2 border border-gray-300 my-4"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
            {error && <p className="error-message">{error}</p>}
            <button className="mt-8 w-2/3 px-8 py-3 rounded-lg border-gray-800 border-2 text-gray-800 font-semibold 
            hover:bg-[#F47D26] hover:border-transparent hover:-translate-y-2 hover:text-white hover:shadow-[0_0_16px_4px_rgba(244,125,38,0.5)]
            transition-all shadow will-change-transform" type="submit">Sign Up</button>
            </form>
            <div className="mt-4 text-gray-600">
              Already have an account?{' '}
              <a className="text-blue-600 hover:underline" href="/login">
                Log In
              </a>
            </div>
          </div>  
      </div>
    <Footer />
    </div>
  );
};