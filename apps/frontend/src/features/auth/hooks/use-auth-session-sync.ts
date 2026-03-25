import { useEffect } from "react";
import { useNavigate } from "@tanstack/react-router";
import { useMe } from "@camper-rent/api-client";

import { parseAuthRedirectTo } from "../../../routes/auth";
import { env } from "../../../utils/env";
import { useAuthStore } from "../../../store/auth-store";

export function useAuthSessionSync() {
  const navigate = useNavigate();
  const auth = useAuthStore((state) => state.auth);
  const syncCurrentUser = useAuthStore((state) => state.syncCurrentUser);
  const clearAuth = useAuthStore((state) => state.clearAuth);

  const currentUserQuery = useMe({
    query: {
      queryKey: ["auth", "me", auth?.token] as unknown as any,
      enabled: Boolean(auth?.token),
      refetchInterval: 30_000,
      refetchIntervalInBackground: true,
      retry: false
    },
    client: auth?.token
      ? {
          baseURL: env.apiBaseUrl,
          headers: { Authorization: `Bearer ${auth.token}` }
        }
      : {
          baseURL: env.apiBaseUrl
        }
  });

  useEffect(() => {
    if (currentUserQuery.data) {
      syncCurrentUser(currentUserQuery.data);
    }
  }, [currentUserQuery.data, syncCurrentUser]);

  useEffect(() => {
    if (currentUserQuery.isError) {
      clearAuth();
      const pathname = typeof window !== "undefined" ? window.location.pathname : undefined;
      const redirectTo = pathname ? parseAuthRedirectTo(pathname) : undefined;
      navigate({
        to: "/auth",
        ...(redirectTo ? { search: { redirectTo } } : {}),
        replace: true
      });
    }
  }, [currentUserQuery.isError, clearAuth, navigate]);
}
