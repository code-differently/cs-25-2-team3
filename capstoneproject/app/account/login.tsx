import React, { useState } from 'react';
import { signInWithEmailAndPassword } from "firebase/auth";
import { firebaseAuth } from "../firebase";
import './account.css';

export const LoginPage: React.FC = () => {
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [error, setError] = useState<string | null>(null);

 const handlesignin = async (e: React.FormEvent) => {
     e.preventDefault();
     setError(null);
 try {
  await signInWithEmailAndPassword(firebaseAuth, email, password)
    .then((userCredential) => {
    // Signed in 
    alert('User successfully logged in!');
    const user = userCredential.user;
    // ...
    });
   } catch (err: any){
      setError(err.message);
      console.error('Login error:', err);
    }
};
    

return (
    <div className="login-container">
      <h1><em>@#$%&&^%$^</em></h1>
      <form onSubmit={handlesignin}>
        <div className="form-group">
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        {error && <p className="error-message">{error}</p>}
        <button type="submit">Log in</button>
      </form>

    </div>
  );
};

