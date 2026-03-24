import { createRootRoute } from "@tanstack/react-router";

import { MainLayout } from "../components/layout/MainLayout";
import { AuthSessionSync } from "../features/auth/AuthSessionSync";

export const Route = createRootRoute({
  component: RootLayout
});

function RootLayout() {
  return (
    <>
      <AuthSessionSync />
      <MainLayout />
    </>
  );
}
