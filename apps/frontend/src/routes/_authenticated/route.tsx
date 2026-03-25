import { Outlet, createFileRoute, redirect } from "@tanstack/react-router";

import { parseAuthRedirectTo } from "../auth";
import { getStoredAuth } from "../../store/auth-store";

export const Route = createFileRoute("/_authenticated")({
  beforeLoad: ({ location }) => {
    if (!getStoredAuth()) {
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
