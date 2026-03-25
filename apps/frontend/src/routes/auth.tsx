import { createFileRoute } from "@tanstack/react-router";

import type { FileRouteTypes } from "../routeTree.gen";
import { AuthPage } from "../features/auth/AuthPage";

export type AuthRedirectTo = Exclude<FileRouteTypes["fullPaths"], "/auth">;

const AUTH_REDIRECT_PATHS = [
  "/",
  "/fleet",
  "/reservations",
] as const satisfies readonly AuthRedirectTo[];

export function parseAuthRedirectTo(raw: unknown): AuthRedirectTo | undefined {
  if (typeof raw !== "string" || raw.length === 0) return undefined;
  return (AUTH_REDIRECT_PATHS as readonly string[]).includes(raw)
    ? (raw as AuthRedirectTo)
    : undefined;
}

export type AuthSearch = {
  redirectTo?: AuthRedirectTo;
};

export const Route = createFileRoute("/auth")({
  validateSearch: (search: Record<string, unknown>): AuthSearch => {
    const redirectTo = parseAuthRedirectTo(search.redirectTo);
    return redirectTo ? { redirectTo } : {};
  },
  component: AuthPage,
});
