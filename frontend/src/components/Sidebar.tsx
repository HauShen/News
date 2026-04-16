"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

const links = [
  { href: "/", label: "Home" },
  { href: "/science", label: "Science" },
  { href: "/dashboard", label: "Dashboard" },
  { href: "/users", label: "Users" },
];

export function Sidebar() {
  const pathname = usePathname();

  return (
    <aside className="w-full border-b border-slate-200 bg-slate-900 px-4 py-4 text-slate-100 md:min-h-screen md:w-64 md:border-b-0 md:border-r">
      <p className="text-lg font-semibold">News Console</p>
      <nav className="mt-4 grid grid-cols-2 gap-2 md:grid-cols-1">
        {links.map((link) => {
          const active = pathname === link.href;
          return (
            <Link
              key={link.href}
              href={link.href}
              className={`rounded-lg px-3 py-2 text-sm transition ${
                active ? "bg-blue-600 text-white" : "bg-slate-800 text-slate-200 hover:bg-slate-700"
              }`}
            >
              {link.label}
            </Link>
          );
        })}
      </nav>
    </aside>
  );
}
