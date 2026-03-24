import { create } from "zustand";
import type { CurrentUserDto, JwtResponseDto } from "@camper-rent/api-client";
import { z } from "zod";

const STORAGE_KEY = "camper-rent-auth";

const jwtResponseSchema = z.object({
  token: z.string().min(1),
  publicId: z.string().uuid(),
  email: z.string().email(),
  roles: z.array(z.string())
});

type AuthState = {
  auth: JwtResponseDto | null;
  setAuth: (auth: JwtResponseDto) => void;
  syncCurrentUser: (currentUser: CurrentUserDto) => void;
  clearAuth: () => void;
};

function readInitialAuth(): JwtResponseDto | null {
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) return null;

  try {
    const parsed = JSON.parse(raw);
    const result = jwtResponseSchema.safeParse(parsed);
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

export const useAuthStore = create<AuthState>((set) => ({
  auth: readInitialAuth(),
  setAuth: (auth) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(auth));
    set({ auth });
  },
  syncCurrentUser: (currentUser) => {
    set((state) => {
      if (!state.auth) {
        return state;
      }
      const nextAuth: JwtResponseDto = {
        ...state.auth,
        publicId: currentUser.publicId,
        email: currentUser.email,
        roles: currentUser.roles
      };
      localStorage.setItem(STORAGE_KEY, JSON.stringify(nextAuth));
      return { auth: nextAuth };
    });
  },
  clearAuth: () => {
    localStorage.removeItem(STORAGE_KEY);
    set({ auth: null });
  }
}));
