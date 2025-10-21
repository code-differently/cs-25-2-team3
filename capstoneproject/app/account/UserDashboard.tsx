import React, { useState } from 'react';

const UserDashboard: React.FC = () => {
  // useState with a boolean type explicitly defined for isAdmin
  const [isAdmin, setIsAdmin] = useState<boolean>(false);
  const [isAnonymous, setIsAnonymous] = useState<boolean>(true);


  // Function to toggle the user role
  const toggleUserSetting = () => {
    setIsAnonymous(prevIsAnonymous => !prevIsAnonymous);
  };

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

export default UserDashboard;