import { useEffect, useState } from "react";
import { Link } from "react-router";

export function NavBar() {
  const [isAnonymous, setAnonymous] = useState<string>("");

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
    <nav className="flex items-center justify-between px-8 py-4 border-b border-gray-200 dark:border-gray-800">
      <div className="flex items-center gap-8">
        <Link to="/" className="text-2xl font-bold text-gray-800 dark:text-gray-100 hover:text-blue-600 transition-colors">
          Forum Hub
        </Link>
        <div className="flex items-center gap-6">
          <Link to="/create-forum" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 transition-colors">
            Create Forum
          </Link>
          <Link to="/forums" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 transition-colors">
            Forums
          </Link>
        </div>
      </div>
      <div className="flex items-center gap-4">
        <Link to="/signup" className="px-5 py-2 rounded-full bg-blue-600 text-white font-semibold hover:bg-blue-700 transition-colors shadow">
          Sign Up
        </Link>
      </div>
      {isAnonymous === "false" ? ( <div>
        NAME
      </div>): (
        <div>
          User Is Anonymous
        </div>
      )}
      <button
        className="px-5 py-2 rounded-full bg-blue-600 text-white font-semibold hover:bg-blue-700 transition-colors shadow"
        // onClick={handleSignIn} // TODO: @Nicole
      >
        Sign In
      </button>
    </nav>
  );
}
