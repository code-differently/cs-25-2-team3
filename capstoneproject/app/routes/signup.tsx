import { SignupPage } from "../signup/signuppage";
import type { Route } from "./+types/signup";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "Signup" },
        { name: "description", content: "Browse and participate in forums." },
    ];
}

export default function Signup() {
    return <SignupPage />;
}