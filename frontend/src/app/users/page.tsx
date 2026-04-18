"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { ErrorMessage } from "@/components/ErrorMessage";
import { FormInput } from "@/components/FormInput";
import { userApi } from "@/lib/api";
import { useAuth } from "@/lib/auth-context";
import type { UserResponse, UserRole } from "@/types";
import { useToast } from "@/components/Toast";

const roles: UserRole[] = ["ADMIN", "NEWS_POSTER", "READER"];

export default function UsersPage() {
  const { user, isAuthenticated } = useAuth();
  const router = useRouter();
  const [name, setName] = useState("");
  const [age, setAge] = useState("18");
  const [role, setRole] = useState<UserRole>("READER");
  const [lookupId, setLookupId] = useState("");
  const [foundUser, setFoundUser] = useState<UserResponse | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [creating, setCreating] = useState(false);
  const { showToast } = useToast();

  useEffect(() => {
    if (!isAuthenticated || user?.role !== "ADMIN") {
      router.push("/login");
    }
  }, [isAuthenticated, user, router]);

  if (!isAuthenticated || user?.role !== "ADMIN") return null;

  async function createUser(event: React.FormEvent) {
    event.preventDefault();
    setError(null);

    if (!name.trim() || !age.trim()) {
      setError("Name and age are required.");
      return;
    }

    const ageValue = Number(age);
    if (Number.isNaN(ageValue) || ageValue <= 0) {
      setError("Age must be a positive number.");
      return;
    }

    try {
      setCreating(true);
      const created = await userApi.create({ name: name.trim(), age: ageValue, role });
      setFoundUser(created);
      setLookupId(created.userId);
      setName("");
      setAge("18");
      setRole("READER");
      showToast("User created successfully.");
    } catch (err) {
      setError((err as Error).message || "Unable to create user.");
      showToast("Failed to create user.", "error");
    } finally {
      setCreating(false);
    }
  }

  async function lookupUser() {
    if (!lookupId.trim()) {
      setError("Enter a user ID to look up.");
      return;
    }

    try {
      setError(null);
      const u = await userApi.getById(lookupId.trim());
      setFoundUser(u);
    } catch (err) {
      setFoundUser(null);
      setError((err as Error).message || "Unable to fetch user.");
    }
  }

  async function deleteUser() {
    if (!foundUser) return;
    try {
      await userApi.delete(foundUser.userId);
      setFoundUser(null);
      setLookupId("");
      showToast("User deleted successfully.");
    } catch (err) {
      setError((err as Error).message || "Unable to delete user.");
      showToast("Failed to delete user.", "error");
    }
  }

  return (
    <section className="grid gap-6 lg:grid-cols-2">
      <div className="space-y-4 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
        <h2 className="text-xl font-semibold">Create User</h2>
        <form onSubmit={createUser} className="space-y-3">
          <FormInput label="Name" value={name} onChange={(e) => setName(e.target.value)} placeholder="John Doe" />
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
              {roles.map((roleOption) => (
                <option key={roleOption} value={roleOption}>
                  {roleOption}
                </option>
              ))}
            </select>
          </label>
          <button
            type="submit"
            disabled={creating}
            className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white disabled:opacity-60"
          >
            {creating ? "Creating..." : "Create User"}
          </button>
        </form>
      </div>

      <div className="space-y-4 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
        <h2 className="text-xl font-semibold">Find User</h2>
        <div className="space-y-3">
          <FormInput
            label="User ID"
            value={lookupId}
            onChange={(e) => setLookupId(e.target.value)}
            placeholder="Paste user ID"
          />
          <button onClick={lookupUser} className="rounded-lg bg-slate-900 px-4 py-2 text-sm font-medium text-white">
            Lookup User
          </button>
        </div>

        {foundUser ? (
          <div className="rounded-lg border border-slate-200 bg-slate-50 p-4 text-sm text-slate-700">
            <p>
              <span className="font-semibold">Name:</span> {foundUser.name}
            </p>
            <p>
              <span className="font-semibold">User ID:</span> {foundUser.userId}
            </p>
            <p>
              <span className="font-semibold">Email:</span> {foundUser.email}
            </p>
            <p>
              <span className="font-semibold">Age:</span> {foundUser.age}
            </p>
            <p>
              <span className="font-semibold">Role:</span> {foundUser.role}
            </p>
            <button
              onClick={deleteUser}
              className="mt-3 rounded-lg bg-red-600 px-4 py-2 text-sm font-medium text-white"
            >
              Delete User
            </button>
          </div>
        ) : null}
      </div>

      {error ? (
        <div className="lg:col-span-2">
          <ErrorMessage message={error} />
        </div>
      ) : null}
    </section>
  );
}
