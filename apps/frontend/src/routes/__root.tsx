import {
  Outlet,
  createRootRouteWithContext,
  redirect,
  useRouterState,
} from "@tanstack/react-router";

import { MainLayout } from "../components/layout/MainLayout";
import { AuthSessionSync } from "../features/auth/AuthSessionSync";
import type { RouterContext } from "../router";

export const Route = createRootRouteWithContext<RouterContext>()({
  beforeLoad: ({ location, context }) => {
    const session = context.auth;
    const isAuthRoute = location.pathname === "/auth";

    if (session && isAuthRoute) {
      throw redirect({ to: "/", replace: true });
    }
  },
  component: RootLayout,
});

function RootLayout() {
  const pathname = useRouterState({ select: (s) => s.location.pathname });
  const isAuthPage = pathname === "/auth";

  return (
    <>
      <AuthSessionSync />
      {isAuthPage ? <Outlet /> : <MainLayout />}
    </>
  );
}
