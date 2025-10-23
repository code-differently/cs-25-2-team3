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
    <nav className="flex items-center justify-between px-8 border-b border-gray-200 h-32">
      <div className="flex items-center gap-3">
        <button className="ml-2 text-2xl font-bold text-gray-800" 
          onClick={() => {
            window.location.href = "/";
          }}>
          <img src="./DevTalkLogoSmall.png" alt="DevTalk Logo" className="w-48 inline-block mr-2"/>
        </button>
      </div>
      {isAnonymous === "false" ? ( <div>
        NAME
      </div>): (
        <div>
          User Is Anonymous
        </div>
      )}
      <button
        className="px-10 py-3 rounded-lg bg-[#F47D26] text-white font-semibold hover:bg-[#f7a163] transition-colors shadow"
        onClick={() => {
          window.location.href = "/login";
        }}
      >
        Log In
      </button>
    </nav>
  );
}
