import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [
    index("routes/home.tsx"),
    route("forums", "routes/forums.tsx"),
    route("forums/create", "routes/create-forum.tsx"),
    route("forums/:forumId", "routes/forum-detail.tsx"),
    route("signup","routes/signup.tsx"),
    route("login","routes/login.tsx" )
] satisfies RouteConfig;
