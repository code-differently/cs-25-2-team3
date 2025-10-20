import { Link } from "react-router";

export function NavBar() {
  return (
    <nav className="flex items-center justify-between px-8 py-4 border-b border-gray-200 dark:border-gray-800">
      <div className="flex items-center gap-8">
        <Link to="/" className="text-2xl font-bold text-gray-800 dark:text-gray-100 hover:text-blue-600 transition-colors">
          Forum Hub
        </Link>
        <div className="flex items-center gap-6">
          <Link to="/forums" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 transition-colors">
            Forums
          </Link>
          <Link to="/forums/create" className="text-gray-600 dark:text-gray-300 hover:text-blue-600 transition-colors">
            Create Forum
          </Link>
        </div>
      </div>
      <div className="flex items-center gap-4">
        <Link to="/signup" className="px-5 py-2 rounded-full bg-blue-600 text-white font-semibold hover:bg-blue-700 transition-colors shadow">
          Sign Up
        </Link>
      </div>
    </nav>
  );
}
