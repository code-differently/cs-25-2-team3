export function NavBar() {
  return (
    <nav className="flex items-center justify-between px-8 py-4 border-b border-gray-200">
      <div className="flex items-center gap-3">
        <button className="ml-2 text-2xl font-bold text-gray-800" 
          onClick={() => {
            window.location.href = "/";
          }}>
          TITLE_GOES_HERE
        </button>
      </div>
      <button
        className="px-5 py-2 rounded-lg bg-[#F47D26] text-white font-semibold hover:bg-[#f7a163] transition-colors shadow"
        onClick={() => {
          window.location.href = "/login";
        }}
      >
        Log In
      </button>
    </nav>
  );
}
