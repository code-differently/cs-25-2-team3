import { Footer } from "~/components/footer/footer";
import { NavBar } from "../components/navbar/navbar";

export function HomePage() {
  return (
    <div className="min-h-screen bg-white dark:bg-gray-900 flex flex-col">

      <NavBar />

      {/* Hero Section */}
      <main className="flex-1 flex flex-col items-center justify-center text-center px-4">
        <h1 className="text-4xl md:text-5xl font-extrabold text-gray-900 dark:text-white mt-16 mb-4">
            TITLE_GOES_HERE
        </h1>
        <p className="text-lg md:text-xl text-gray-600 dark:text-gray-300 max-w-2xl mb-8">
          Create or join forums, respond with messages and likes, and view AI-generated summaries.
          Sign in to get started!
        </p>
        <div className="flex flex-col md:flex-row gap-4 justify-center mb-12">
          <button
            className="px-8 py-3 rounded-full bg-blue-600 text-white font-semibold hover:bg-blue-700 transition-colors shadow"
            // onClick={handleCreateForum} // TODO: @Jayden
          >
            Create Forum
          </button>
          <button
            className="px-8 py-3 rounded-full bg-gray-200 dark:bg-gray-700 text-gray-800 dark:text-gray-200 font-semibold hover:bg-gray-300 dark:hover:bg-gray-600 transition-colors shadow"
            onClick={() => {
              window.location.href = "/forums";
            }}
          >
            Browse Forums
          </button>
        </div>
        <div className="max-w-xl mx-auto bg-blue-50 dark:bg-gray-800 rounded-2xl p-6 mt-4 border border-blue-100 dark:border-gray-700">
          <h2 className="text-xl font-semibold mb-2 text-blue-700 dark:text-blue-400">
            How it works
          </h2>
          <ul className="text-left text-gray-700 dark:text-gray-300 list-disc list-inside space-y-1">
            <li>Create an account or sign in to your existing account</li>
            <li>Start or join a forum about any question or problem</li>
            <li>Respond to responses and upvote and downvote messages</li>
            <li>Read AI generated summaries of discussions after the forum ends </li>
          </ul>
        </div>
      </main>

      <Footer />
      
    </div>
  );
}
