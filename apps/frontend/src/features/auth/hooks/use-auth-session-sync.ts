import { useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import { me } from "@camper-rent/api-client";

import { env } from "../../../utils/env";
import { useAuthStore } from "../../../store/auth-store";

export function useAuthSessionSync() {
  const auth = useAuthStore((state) => state.auth);
  const syncCurrentUser = useAuthStore((state) => state.syncCurrentUser);
  const clearAuth = useAuthStore((state) => state.clearAuth);

  const currentUserQuery = useQuery({
    queryKey: ["auth", "me", auth?.token],
    queryFn: () => me(env.apiBaseUrl, auth!.token),
    enabled: Boolean(auth?.token),
    refetchInterval: 30_000,
    refetchIntervalInBackground: true,
    retry: false
  });

  useEffect(() => {
    if (currentUserQuery.data) {
      syncCurrentUser(currentUserQuery.data);
    }
  }, [currentUserQuery.data, syncCurrentUser]);

  useEffect(() => {
    if (currentUserQuery.isError) {
      clearAuth();
    }
  }, [currentUserQuery.isError, clearAuth]);
}
