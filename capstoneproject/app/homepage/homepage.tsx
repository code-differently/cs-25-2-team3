import { Footer } from "../components/footer/footer";
import { NavBar } from "../components/navbar/navbar";

export function HomePage() {
  return (
    <div className="min-h-screen bg-white flex-col">

      <NavBar />

      {/* Hero Section */}
      <main className="flex-1 flex flex-col items-center justify-center text-center px-4">
        <h1 className="text-4xl md:text-5xl font-extrabold text-gray-900 mt-16 mb-4">
            TITLE_GOES_HERE
        </h1>
        <p className="text-lg md:text-xl text-gray-600 max-w-2xl mb-8">
          Create or join forums, respond with messages and likes, and view AI-generated summaries.
          Sign in to get started!
        </p>
        <div className="flex flex-col md:flex-row gap-4 justify-center mb-12">
          <button
            className="px-8 py-3 rounded-lg border-gray-800 border-2 text-gray-800 font-semibold 
            hover:bg-[#F47D26] hover:border-transparent hover:-translate-y-2 hover:text-white hover:shadow-[0_0_16px_4px_rgba(244,125,38,0.5)]
            transition-all shadow will-change-transform"
            // onClick={handleCreateForum} // TODO: @Jayden
          >
            Create Forum
          </button>
          <button
            className="px-8 py-3 rounded-full bg-gray-200 text-gray-800 font-semibold hover:bg-gray-300 transition-colors shadow"
            onClick={() => {
              window.location.href = "/forums";
            }}
          >
            Browse Forums
          </button>
        </div>
        <div className="max-w-xl mx-auto bg-blue-50 rounded-2xl p-6 mt-4 border border-blue-100">
          <h2 className="text-xl font-semibold mb-2 text-blue-700">
            How it works
          </h2>
          <ul className="text-left text-gray-700 list-disc list-inside space-y-1">
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
