"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { ArticleCard } from "@/components/ArticleCard";
import { ErrorMessage } from "@/components/ErrorMessage";
import { LoadingSpinner } from "@/components/LoadingSpinner";
import { articleApi } from "@/lib/api";
import { useAuth } from "@/lib/auth-context";
import type { ArticleResponse } from "@/types";

export default function ArticlesPage() {
  const { isAuthenticated } = useAuth();
  const router = useRouter();
  const [articles, setArticles] = useState<ArticleResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!isAuthenticated) {
      router.push("/login");
      return;
    }
    articleApi
      .getAllSortedByLikes()
      .then(setArticles)
      .catch((err: Error) => setError(err.message || "Unable to load articles"))
      .finally(() => setLoading(false));
  }, [isAuthenticated, router]);

  if (!isAuthenticated) return null;

  return (
    <section className="space-y-6">
      <div>
        <h2 className="text-2xl font-semibold">All Articles</h2>
        <p className="mt-1 text-sm text-slate-600">Articles sorted by most liked</p>
      </div>
      {loading ? <LoadingSpinner /> : null}
      {error ? <ErrorMessage message={error} /> : null}
      {!loading && !error ? (
        articles.length > 0 ? (
          <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
            {articles.map((article) => (
              <ArticleCard key={article.oid} article={article} />
            ))}
          </div>
        ) : (
          <p className="text-sm text-slate-500">No articles found.</p>
        )
      ) : null}
    </section>
  );
}
