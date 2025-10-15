export function NavBar() {
  return (
    <nav className="flex items-center justify-between px-8 py-4 border-b border-gray-200 dark:border-gray-800">
      <div className="flex items-center gap-3">
        <button className="ml-2 text-2xl font-bold text-gray-800 dark:text-gray-100" 
          onClick={() => {
            window.location.href = "/";
          }}>
          TITLE_GOES_HERE
        </button>
      </div>
      <button
        className="px-5 py-2 rounded-full bg-blue-600 text-white font-semibold hover:bg-blue-700 transition-colors shadow"
        // onClick={handleSignIn} // TODO: @Nicole
      >
        Sign In
      </button>
    </nav>
  );
}
