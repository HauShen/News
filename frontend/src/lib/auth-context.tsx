"use client";

import { createContext, useContext, useSyncExternalStore, useCallback, useRef } from "react";
import type { AuthResponse } from "@/types";

interface AuthContextValue {
  user: AuthResponse | null;
  token: string | null;
  login: (authResponse: AuthResponse) => void;
  logout: () => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

const AUTH_STORAGE_KEY = "news_auth";

function getSnapshot(): AuthResponse | null {
  if (typeof window === "undefined") return null;
  try {
    const stored = localStorage.getItem(AUTH_STORAGE_KEY);
    if (stored) return JSON.parse(stored) as AuthResponse;
  } catch {
    // ignore
  }
  return null;
}

function getServerSnapshot(): AuthResponse | null {
  return null;
}

let listeners: Array<() => void> = [];

function emitChange() {
  for (const listener of listeners) {
    listener();
  }
}

function subscribe(listener: () => void) {
  listeners = [...listeners, listener];
  return () => {
    listeners = listeners.filter((l) => l !== listener);
  };
}

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const cachedUser = useRef<AuthResponse | null | undefined>(undefined);

  const user = useSyncExternalStore(subscribe, () => {
    const snap = getSnapshot();
    // Avoid creating new object reference on every render when value hasn't changed
    if (cachedUser.current !== undefined && JSON.stringify(cachedUser.current) === JSON.stringify(snap)) {
      return cachedUser.current;
    }
    cachedUser.current = snap;
    return snap;
  }, getServerSnapshot);

  const token = user?.token ?? null;

  const login = useCallback((authResponse: AuthResponse) => {
    localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(authResponse));
    cachedUser.current = undefined; // invalidate cache
    emitChange();
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem(AUTH_STORAGE_KEY);
    cachedUser.current = undefined; // invalidate cache
    emitChange();
  }, []);

  return (
    <AuthContext.Provider value={{ user, token, login, logout, isAuthenticated: !!token }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
