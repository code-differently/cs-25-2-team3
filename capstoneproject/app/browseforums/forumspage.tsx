import { Footer } from "../components/footer/footer";
import { NavBar } from "../components/navbar/navbar";

export function ForumsPage() {
  // Placeholder forum data
  const forums = [
    {
      id: 1,
      title: "How to learn React?",
      description: "Share your best resources and tips for learning React.",
      createdBy: "Alice",
      createdAt: "2025-10-10",
      status: "Open",
    },
    {
      id: 2,
      title: "Best AI tools for students",
      description: "Discuss the most useful AI tools for academic success.",
      createdBy: "Bob",
      createdAt: "2025-10-12",
      status: "Closed",
    },
  ];

  // Placeholder for filter state (not implemented)
  // const [filter, setFilter] = React.useState("");

  return (
    <div className="min-h-screen bg-white flex flex-col">
     
      <NavBar />

      {/* IMPLEMENT SOME FILTER FEATURE TODO: @Tyran */}
      <main className="flex-1 px-8 py-6">
        <ul className="space-y-6 max-w-2xl mx-auto">
          {forums.map((forum) => (
            <li
              key={forum.id}
              className="rounded-xl border border-gray-200 bg-white p-6 shadow hover:shadow-lg transition-shadow"
            >
              <div className="flex items-center justify-between mb-2">
                <h2 className="text-xl font-semibold text-blue-700">{forum.title}</h2>
                <span className={`text-xs px-2 py-1 rounded-full font-bold ${forum.status === "Open" ? "bg-green-100 text-green-700" : "bg-gray-200 text-gray-600"}`}>
                  {forum.status}
                </span>
              </div>
              <p className="text-gray-700 mb-2">{forum.description}</p>
              <div className="text-sm text-gray-500">
                By {forum.createdBy} • {forum.createdAt}
              </div>
            </li>
          ))}
        </ul>
      </main>

      <Footer />

    </div>
  );
}
