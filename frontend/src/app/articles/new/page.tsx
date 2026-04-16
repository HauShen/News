"use client";

import { useState } from "react";
import { FormInput } from "@/components/FormInput";
import { articleApi } from "@/lib/api";
import type { ArticleResponse } from "@/types";
import { ArticleCard } from "@/components/ArticleCard";
import { ErrorMessage } from "@/components/ErrorMessage";
import { useToast } from "@/components/Toast";

export default function NewArticlePage() {
  const [userId, setUserId] = useState("");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [createdArticle, setCreatedArticle] = useState<ArticleResponse | null>(null);
  const { showToast } = useToast();

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);

    if (!userId.trim() || !title.trim() || !content.trim()) {
      setError("All fields are required.");
      return;
    }

    try {
      setSubmitting(true);
      const created = await articleApi.create(userId.trim(), { title: title.trim(), content: content.trim() });
      setCreatedArticle(created);
      setTitle("");
      setContent("");
      showToast("Article created successfully.");
    } catch (err) {
      setError((err as Error).message || "Unable to create article.");
      showToast("Failed to create article.", "error");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <section className="max-w-2xl space-y-6">
      <div>
        <h2 className="text-2xl font-semibold">Create Article</h2>
        <p className="mt-1 text-sm text-slate-600">Create a new article by user ID</p>
      </div>

      {error ? <ErrorMessage message={error} /> : null}

      <form onSubmit={handleSubmit} className="space-y-4 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
        <FormInput label="User ID" value={userId} onChange={(e) => setUserId(e.target.value)} placeholder="e.g. uuid" />
        <FormInput label="Title" value={title} onChange={(e) => setTitle(e.target.value)} placeholder="Article title" />
        <FormInput
          label="Content"
          textarea
          value={content}
          onChange={(e) => setContent(e.target.value)}
          placeholder="Write your article content"
        />
        <button
          type="submit"
          disabled={submitting}
          className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:bg-blue-400"
        >
          {submitting ? "Creating..." : "Create Article"}
        </button>
      </form>

      {createdArticle ? <ArticleCard article={createdArticle} /> : null}
    </section>
  );
}
