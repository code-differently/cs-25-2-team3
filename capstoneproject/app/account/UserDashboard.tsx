import React, { useEffect, useState } from 'react';

export const UserDashboardPage: React.FC = () => {
  // useState with a boolean type explicitly defined for isAdmin
  const [isAdmin, setIsAdmin] = useState<boolean>(false);
  const [isAnonymous, setAnonymous] = useState<string>("");


  // Function to toggle the user role
  const toggleUserSetting = () => {
    if(isAnonymous === "false") {
      sessionStorage.setItem("anonymous", "true");
      setAnonymous("true");

    }
    else {
      sessionStorage.setItem("anonymous", "false");
      setAnonymous("false")
    }
    
  };

    // Load from sessionStorage on component mount (client-side only)
    useEffect(() => {
      if (typeof window !== "undefined") {
        const savedAnonymous = window.sessionStorage.getItem("anonymous");
        if (savedAnonymous !== null) {
          setAnonymous(savedAnonymous);
          console.log("Anonymous User: " + isAnonymous);
        }
      }
    }, [isAnonymous]);
  
  return (
    
    <div>
      <h1>Welcome</h1>
      {/* Conditionally render content based on isAdmin state */}
      {isAdmin ? (
        <div>
          <h2>Admin View</h2>
          <p>You can edit or delete.</p>
        </div>
      ) : (
        <div>
          <h2> User View</h2>
          <p>
            {isAnonymous
              ? "You are browsing anonymously."
              : "Your identity is visible to others."}
          </p>
          <button onClick={toggleUserSetting}>
            {isAnonymous ? "Go Visible" : "Go Anonymous"}
            </button>
        </div>
      )}
    </div>
  );
};
