import { LoginPage } from "~/account/login";
import type { Route } from "./+types/login";




export function meta({}: Route.MetaArgs) {
    return [
        { title: "Login" },
        { name: "description", content: "Browse and participate in forums." },
    ];
}

export default function Login() {
    return <LoginPage />;
}