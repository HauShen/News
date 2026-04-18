"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { ArticleCard } from "@/components/ArticleCard";
import { ErrorMessage } from "@/components/ErrorMessage";
import { FormInput } from "@/components/FormInput";
import { articleApi, emailApi } from "@/lib/api";
import { useAuth } from "@/lib/auth-context";
import type { ArticleResponse } from "@/types";
import { useToast } from "@/components/Toast";

export default function DashboardPage() {
  const { user, isAuthenticated } = useAuth();
  const router = useRouter();
  const [articleId, setArticleId] = useState("");
  const [editTitle, setEditTitle] = useState("");
  const [editContent, setEditContent] = useState("");
  const [article, setArticle] = useState<ArticleResponse | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [emailAddress, setEmailAddress] = useState("");
  const [sendingEmail, setSendingEmail] = useState(false);
  const { showToast } = useToast();

  const canManageArticles = user?.role === "NEWS_POSTER" || user?.role === "ADMIN";

  useEffect(() => {
    if (!isAuthenticated) {
      router.push("/login");
    }
  }, [isAuthenticated, router]);

  if (!isAuthenticated) return null;

  async function fetchArticle() {
    if (!articleId.trim()) {
      setError("Please enter an article ID.");
      return;
    }
    setError(null);
    try {
      setLoading(true);
      const result = await articleApi.getById(articleId.trim());
      setArticle(result);
      setEditTitle(result.title);
      setEditContent(result.content);
    } catch (err) {
      setArticle(null);
      setError((err as Error).message || "Unable to fetch article.");
    } finally {
      setLoading(false);
    }
  }

  async function updateArticle() {
    if (!article || !editTitle.trim() || !editContent.trim()) {
      setError("Title and content are required to update.");
      return;
    }
    try {
      setError(null);
      await articleApi.update(article.oid, { title: editTitle.trim(), content: editContent.trim() });
      const refreshed = await articleApi.getById(article.oid);
      setArticle(refreshed);
      showToast("Article updated successfully.");
    } catch (err) {
      setError((err as Error).message || "Unable to update article.");
      showToast("Failed to update article.", "error");
    }
  }

  async function deleteArticle() {
    if (!article) return;
    try {
      await articleApi.delete(article.oid);
      setArticle(null);
      setEditTitle("");
      setEditContent("");
      showToast("Article deleted successfully.");
    } catch (err) {
      setError((err as Error).message || "Unable to delete article.");
      showToast("Failed to delete article.", "error");
    }
  }

  async function sendNewsletter() {
    if (!emailAddress.trim() || !user) {
      setError("Please enter an email address.");
      return;
    }
    try {
      setSendingEmail(true);
      setError(null);
      await emailApi.sendWithFeedback({ emailAddress: emailAddress.trim(), userId: user.userId });
      showToast("Newsletter sent successfully!");
      setEmailAddress("");
    } catch (err) {
      setError((err as Error).message || "Failed to send email.");
      showToast("Failed to send newsletter.", "error");
    } finally {
      setSendingEmail(false);
    }
  }

  return (
    <section className="space-y-6">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h2 className="text-2xl font-semibold">Dashboard</h2>
          <p className="mt-1 text-sm text-slate-600">
            Logged in as <span className="font-medium">{user?.name}</span> ({user?.role})
          </p>
        </div>
        {canManageArticles && (
          <Link href="/articles/new" className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white">
            New Article
          </Link>
        )}
      </div>

      {/* Article lookup */}
      <div className="space-y-3 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
        <h3 className="text-lg font-semibold">Find Article</h3>
        <div className="flex flex-col gap-3 sm:flex-row">
          <div className="flex-1">
            <FormInput
              label="Article ID"
              value={articleId}
              onChange={(e) => setArticleId(e.target.value)}
              placeholder="Enter article ID"
            />
          </div>
          <button
            onClick={fetchArticle}
            disabled={loading}
            className="self-end rounded-lg bg-slate-900 px-4 py-2 text-sm font-medium text-white disabled:opacity-60"
          >
            {loading ? "Loading..." : "Load"}
          </button>
        </div>
      </div>

      {error ? <ErrorMessage message={error} /> : null}

      {article ? (
        <div className="grid gap-4 lg:grid-cols-2">
          <ArticleCard article={article} />
          {canManageArticles ? (
            <div className="space-y-3 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
              <h3 className="text-lg font-semibold">Edit Article</h3>
              <FormInput label="Title" value={editTitle} onChange={(e) => setEditTitle(e.target.value)} />
              <FormInput textarea label="Content" value={editContent} onChange={(e) => setEditContent(e.target.value)} />
              <div className="flex gap-2">
                <button onClick={updateArticle} className="rounded-lg bg-blue-600 px-4 py-2 text-sm text-white">
                  Save Changes
                </button>
                <button onClick={deleteArticle} className="rounded-lg bg-red-600 px-4 py-2 text-sm text-white">
                  Delete
                </button>
              </div>
            </div>
          ) : (
            <div className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
              <p className="text-sm text-slate-600">You have read-only access to articles.</p>
            </div>
          )}
        </div>
      ) : null}

      {/* Email section for NEWS_POSTER / ADMIN */}
      {canManageArticles && (
        <div className="space-y-3 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
          <h3 className="text-lg font-semibold">Send Newsletter</h3>
          <p className="text-sm text-slate-600">Send today&apos;s articles to a reader via email.</p>
          <div className="flex flex-col gap-3 sm:flex-row">
            <div className="flex-1">
              <FormInput
                label="Recipient Email"
                type="email"
                value={emailAddress}
                onChange={(e) => setEmailAddress(e.target.value)}
                placeholder="reader@example.com"
              />
            </div>
            <button
              onClick={sendNewsletter}
              disabled={sendingEmail}
              className="self-end rounded-lg bg-emerald-600 px-4 py-2 text-sm font-medium text-white disabled:opacity-60"
            >
              {sendingEmail ? "Sending..." : "Send"}
            </button>
          </div>
        </div>
      )}
    </section>
  );
}
