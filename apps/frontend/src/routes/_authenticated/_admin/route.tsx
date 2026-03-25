import { Outlet, createFileRoute, redirect } from "@tanstack/react-router";

import { parseAuthRedirectTo } from "../../auth";

const REQUIRED_ROLE = "ADMIN";

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

    const roles = context.auth.roles ?? [];
    if (!roles.includes(REQUIRED_ROLE)) {
      throw redirect({ to: "/", replace: true });
    }
  },
  component: () => <Outlet />
});

