"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { authApi } from "@/lib/api";
import { useAuth } from "@/lib/auth-context";
import { FormInput } from "@/components/FormInput";
import { ErrorMessage } from "@/components/ErrorMessage";
import { useToast } from "@/components/Toast";
import type { UserRole } from "@/types";

const roles: { value: UserRole; label: string }[] = [
  { value: "READER", label: "Reader — View and like articles" },
  { value: "NEWS_POSTER", label: "News Poster — Post and manage articles" },
  { value: "ADMIN", label: "Admin — Full access" },
];

export default function SignupPage() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [age, setAge] = useState("18");
  const [role, setRole] = useState<UserRole>("READER");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const router = useRouter();
  const { showToast } = useToast();

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);

    if (!name.trim() || !email.trim() || !password.trim()) {
      setError("All fields are required.");
      return;
    }

    const ageValue = Number(age);
    if (Number.isNaN(ageValue) || ageValue <= 0) {
      setError("Age must be a positive number.");
      return;
    }

    if (password.length < 6) {
      setError("Password must be at least 6 characters.");
      return;
    }

    try {
      setLoading(true);
      const response = await authApi.signup({
        name: name.trim(),
        email: email.trim(),
        password,
        age: ageValue,
        role,
      });
      login(response);
      showToast("Account created successfully!");
      router.push("/");
    } catch (err) {
      setError((err as Error).message || "Signup failed.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="mx-auto max-w-md space-y-6">
      <div className="text-center">
        <h2 className="text-2xl font-semibold">Sign Up</h2>
        <p className="mt-1 text-sm text-slate-600">Create your account</p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-4 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
        {error ? <ErrorMessage message={error} /> : null}
        <FormInput
          label="Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Your name"
        />
        <FormInput
          label="Email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="you@example.com"
        />
        <FormInput
          label="Password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="Min 6 characters"
        />
        <FormInput
          label="Age"
          type="number"
          value={age}
          onChange={(e) => setAge(e.target.value)}
          min="1"
          placeholder="18"
        />
        <label className="block space-y-1 text-sm text-slate-700">
          <span className="font-medium">Role</span>
          <select
            value={role}
            onChange={(e) => setRole(e.target.value as UserRole)}
            className="w-full rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm"
          >
            {roles.map((r) => (
              <option key={r.value} value={r.value}>
                {r.label}
              </option>
            ))}
          </select>
        </label>
        <button
          type="submit"
          disabled={loading}
          className="w-full rounded-lg bg-blue-600 px-4 py-2.5 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-60"
        >
          {loading ? "Creating account..." : "Sign Up"}
        </button>
        <p className="text-center text-sm text-slate-600">
          Already have an account?{" "}
          <Link href="/login" className="text-blue-600 hover:underline">
            Log in
          </Link>
        </p>
      </form>
    </section>
  );
}
