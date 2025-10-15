import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [
  index("routes/home.tsx"),
  route("forums", "forum/forum.tsx"),
  route("create-forum", "forum/create-forum.tsx"),
] satisfies RouteConfig;
