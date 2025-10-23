import type { Route } from "./+types/userdashboard";


export function meta({}: Route.MetaArgs) {
  return [
    { title: "User Dashboard Page" },
    { name: "description", content: "Welcome, User!" },
  ];
}

export default function UserDashboard() {
  return (
    <div>
      <h1>User Dashboard</h1>
      <p>Welcome to your dashboard!</p>
    </div>
  );
}

