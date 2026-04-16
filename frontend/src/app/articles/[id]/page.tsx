"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { ErrorMessage } from "@/components/ErrorMessage";
import { LoadingSpinner } from "@/components/LoadingSpinner";
import { articleApi } from "@/lib/api";
import type { ArticleResponse } from "@/types";

export default function ArticleDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [article, setArticle] = useState<ArticleResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    articleApi
      .getById(id)
      .then(setArticle)
      .catch((err: Error) => setError(err.message || "Unable to load article"))
      .finally(() => setLoading(false));
  }, [id]);

  return (
    <section className="max-w-3xl space-y-6">
      {loading ? <LoadingSpinner /> : null}
      {error ? <ErrorMessage message={error} /> : null}
      {article ? (
        <article className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
          <p className="text-xs uppercase tracking-wide text-slate-500">Article #{article.oid}</p>
          <h2 className="mt-2 text-2xl font-semibold text-slate-900">{article.title}</h2>
          <p className="mt-4 whitespace-pre-wrap text-sm leading-7 text-slate-700">{article.content}</p>
          <div className="mt-6 grid gap-2 text-xs text-slate-500 sm:grid-cols-2">
            <span>User: {article.userId}</span>
            <span>Likes: {article.likeCount}</span>
            <span>Created: {new Date(article.createdAt).toLocaleString()}</span>
            {article.updatedAt ? <span>Updated: {new Date(article.updatedAt).toLocaleString()}</span> : null}
          </div>
        </article>
      ) : null}
    </section>
  );
}
