import { Outlet, createFileRoute, redirect } from "@tanstack/react-router";

import { parseAuthRedirectTo } from "../../auth";

export const Route = createFileRoute("/_authenticated/_admin")({
  beforeLoad: ({ location, context }) => {
    if (!context.auth) {
      const redirectTo = parseAuthRedirectTo(location.pathname);
      throw redirect({
        to: "/auth",
        ...(redirectTo ? { search: { redirectTo } } : {}),
        replace: true
      });
    }
  },
  component: () => <Outlet />
});

