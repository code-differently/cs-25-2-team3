import { onAuthStateChanged } from "firebase/auth";
import { useEffect, useState } from "react";
import { Link } from "react-router";
import { Footer } from "../components/footer/footer";
import { NavBar } from "../components/navbar/navbar";
import { firebaseAuth } from "../firebase";
import { useForums } from "../hooks/useFirestore";

export function ForumsPage() {
  const { forums, loading, error } = useForums();
  const [user, setCurrentUser] = useState<any>(null);

  // Set up auth state listener once on mount
  useEffect(() => {
      const unsubscribe = onAuthStateChanged(firebaseAuth, (user) => {
      setCurrentUser(user);
      if (!user) {
          // User is redirected to login page
          window.location.href = '/login';
      }
      });
      return () => unsubscribe();
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen bg-white dark:bg-gray-900 flex flex-col">
        <NavBar />
        <main className="flex-1 flex items-center justify-center">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-4 text-gray-600 dark:text-gray-400">Loading forums...</p>
          </div>
        </main>
        <Footer />
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-white dark:bg-gray-900 flex flex-col">
        <NavBar />
        <main className="flex-1 flex items-center justify-center">
          <div className="text-center">
            <p className="text-red-600 dark:text-red-400 mb-4">{error}</p>
            <button 
              onClick={() => window.location.reload()} 
              className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg"
            >
              Retry
            </button>
          </div>
        </main>
        <Footer />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-white flex flex-col">
     
      <NavBar />

      <main className="flex-1 px-8 py-6">
        <div className="max-w-4xl mx-auto">
          <div className="flex justify-between items-center mb-8">
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white">Discussion Forums</h1>
            <Link
              to="/create-forum"
              className="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg transition-colors"
            >
              Create Forum
            </Link>
          </div>

          {forums.length === 0 ? (
            <div className="text-center py-12">
              <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">No forums yet</h3>
              <p className="text-gray-600 dark:text-gray-400 mb-4">Be the first to create a discussion forum!</p>
              <Link
                to="/create-forum"
                className="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg transition-colors"
              >
                Create Your First Forum
              </Link>
            </div>
          ) : (
            <ul className="space-y-6">
              {forums.map((forum) => {
                const createdAt = forum.createdAt?.toDate();
                const endTime = forum.endTime?.toDate();
                const isExpired = endTime && new Date() > endTime;
                const status = isExpired ? "Closed" : "Open";

                return (
                  <li key={forum.id}>
                    <Link
                      to={`/forum/${forum.id}`}
                      className="block rounded-xl border border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 p-6 shadow hover:shadow-lg transition-all hover:scale-[1.02]"
                    >
                      <div className="flex items-start justify-between mb-3">
                        <h2 className="text-xl font-semibold text-blue-700 dark:text-blue-400 hover:text-blue-800 dark:hover:text-blue-300">
                          {forum.title}
                        </h2>
                        <div className="flex flex-col items-end gap-2">
                          <span className={`text-xs px-2 py-1 rounded-full font-bold ${
                            status === "Open" ? "bg-green-100 text-green-700" : "bg-gray-200 text-gray-600"
                          }`}>
                            {status}
                          </span>
                          {forum.tags && forum.tags.length > 0 && (
                            <span className="text-xs px-2 py-1 bg-blue-100 text-blue-700 rounded-full">
                              {forum.tags[0]}
                            </span>
                          )}
                        </div>
                      </div>
                      
                      <p className="text-gray-700 dark:text-gray-300 mb-3 line-clamp-2">
                        {forum.description}
                      </p>
                      
                      <div className="flex justify-between items-center text-sm text-gray-500 dark:text-gray-400">
                        <div>
                          By {forum.creatorName} • {createdAt ? createdAt.toLocaleDateString() : 'Unknown date'}
                        </div>
                        <div className="flex gap-4">
                          <span>{forum.commentCount || 0} responses</span>
                          <span>↑ {forum.upvotes || 0}</span>
                          <span>↓ {forum.downvotes || 0}</span>
                        </div>
                      </div>

                      {endTime && (
                        <div className="mt-2 text-xs text-gray-400">
                          {isExpired ? 'Closed' : 'Closes'} on {endTime.toLocaleDateString()} at {endTime.toLocaleTimeString()}
                        </div>
                      )}
                    </Link>
                  </li>
                );
              })}
            </ul>
          )}
        </div>
      </main>

      <Footer />

    </div>
  );
}
