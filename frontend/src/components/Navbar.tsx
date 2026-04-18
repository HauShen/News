"use client";

import { useAuth } from "@/lib/auth-context";
import Link from "next/link";

export function Navbar() {
  const { user, isAuthenticated, logout } = useAuth();

  return (
    <header className="flex items-center justify-between border-b border-slate-200 bg-white px-6 py-4">
      <div>
        <h1 className="text-xl font-semibold text-slate-900">Enterprise News Portal</h1>
        <p className="mt-1 text-sm text-slate-500">Connected to your Spring Boot News API</p>
      </div>
      <div className="flex items-center gap-4">
        {isAuthenticated && user ? (
          <>
            <div className="text-right text-sm">
              <p className="font-medium text-slate-900">{user.name}</p>
              <p className="text-xs text-slate-500">{user.role}</p>
            </div>
            <button
              onClick={logout}
              className="rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-sm text-slate-700 hover:bg-slate-50"
            >
              Logout
            </button>
          </>
        ) : (
          <div className="flex gap-2">
            <Link
              href="/login"
              className="rounded-lg border border-slate-300 bg-white px-3 py-1.5 text-sm text-slate-700 hover:bg-slate-50"
            >
              Log In
            </Link>
            <Link
              href="/signup"
              className="rounded-lg bg-blue-600 px-3 py-1.5 text-sm font-medium text-white hover:bg-blue-700"
            >
              Sign Up
            </Link>
          </div>
        )}
      </div>
    </header>
  );
}
