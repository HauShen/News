import Link from "next/link";
import type { ArticleResponse } from "@/types";

interface ArticleCardProps {
  article: ArticleResponse;
}

export function ArticleCard({ article }: ArticleCardProps) {
  return (
    <article className="rounded-xl border border-slate-200 bg-white p-5 shadow-sm">
      <h3 className="text-lg font-semibold text-slate-900">{article.title}</h3>
      <p className="mt-2 line-clamp-3 text-sm text-slate-600">{article.content}</p>
      <div className="mt-3 flex flex-wrap gap-4 text-xs text-slate-500">
        <span>Article #{article.oid}</span>
        <span>User: {article.userId}</span>
        <span>Likes: {article.likeCount}</span>
      </div>
      <Link href={`/articles/${article.oid}`} className="mt-4 inline-flex text-sm font-medium text-blue-700">
        Open article →
      </Link>
    </article>
  );
}
