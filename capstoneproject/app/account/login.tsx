import React, { useState } from "react";
import { signInWithEmailAndPassword } from "firebase/auth";
import { firebaseAuth } from "../firebase";

export const LoginPage: React.FC = () => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [error, setError] = useState<string | null>(null);

  const handleSignIn = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
  
  try {
    const userCredential = await signInWithEmailAndPassword(firebaseAuth, email, password);
    const user = userCredential.user;

    const adminUID = "g0035irux7MA70QfUOi3xb57bmg1";

    if (user.uid === adminUID) {
      // Admin user
      sessionStorage.setItem("role", "admin");
      sessionStorage.setItem("admin", "true");
      sessionStorage.setItem("anonymous", "false");
      alert("Admin logged in!");
    } else {
      // Regular signed-in user
      sessionStorage.setItem("role", "user");
      sessionStorage.setItem("admin", "false");
      sessionStorage.setItem("anonymous", "false");
      alert("User logged in!");
    }

  } catch (err: any) {
    setError(err.message);
    console.error("Login error:", err);
  }
};

  return (
    <div className="login-container">
      <h1><em>Log In</em></h1>
      <form onSubmit={handleSignIn}>
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
