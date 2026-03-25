import { create } from "zustand";
import { persist } from "zustand/middleware";
import { generatedApi } from "@camper-rent/api-client";
import type { CurrentUserDto, JwtResponseDto } from "@camper-rent/api-client";
import { z } from "zod";

const STORAGE_KEY = "camper-rent-auth";

const jwtResponseSchema = generatedApi.jwtResponseDtoSchema.extend({
  token: z.string().min(1),
  publicId: z.uuid(),
  email: z.email(),
  roles: z.array(z.string()),
});

type AuthState = {
  auth: JwtResponseDto | null;
  setAuth: (auth: JwtResponseDto) => void;
  syncCurrentUser: (currentUser: CurrentUserDto) => void;
  clearAuth: () => void;
};

export function getStoredAuth(): JwtResponseDto | null {
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) return null;

  try {
    const parsed = JSON.parse(raw);
    const candidate =
      parsed?.state &&
      typeof parsed.state === "object" &&
      parsed.state !== null &&
      "auth" in parsed.state
        ? (parsed.state as { auth?: unknown }).auth
        : parsed;
    const result = jwtResponseSchema.safeParse(candidate);
    if (!result.success) {
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }

    return result.data as JwtResponseDto;
  } catch {
    localStorage.removeItem(STORAGE_KEY);
    return null;
  }
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      auth: getStoredAuth(),
      setAuth: (auth) => set({ auth }),
      syncCurrentUser: (currentUser) => {
        set((state) => {
          if (!state.auth) return state;
          const nextAuth: JwtResponseDto = {
            ...state.auth,
            publicId: currentUser.publicId,
            email: currentUser.email,
            roles: currentUser.roles,
          };
          return { auth: nextAuth };
        });
      },
      clearAuth: () => set({ auth: null }),
    }),
    {
      name: STORAGE_KEY,
      partialize: (state) => ({ auth: state.auth }),
      merge: (persisted, current) => {
        if (
          typeof persisted !== "object" ||
          persisted === null ||
          !("auth" in persisted)
        ) {
          localStorage.removeItem(STORAGE_KEY);
          return current;
        }
        const result = jwtResponseSchema.safeParse(persisted.auth);
        if (!result.success) {
          localStorage.removeItem(STORAGE_KEY);
          return current;
        }
        return {
          ...current,
          auth: result.data,
        };
      },
    },
  ),
);
