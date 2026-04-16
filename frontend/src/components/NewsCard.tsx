import Link from "next/link";
import type { NewsStory } from "@/types";

interface NewsCardProps {
  story: NewsStory;
}

export function NewsCard({ story }: NewsCardProps) {
  return (
    <article className="rounded-xl border border-slate-200 bg-white p-5 shadow-sm transition hover:shadow-md">
      {story.section ? (
        <p className="mb-2 text-xs font-semibold uppercase tracking-wide text-blue-700">{story.section}</p>
      ) : null}
      <h3 className="text-lg font-semibold text-slate-900">{story.title}</h3>
      {story.abstractText ? <p className="mt-2 text-sm text-slate-600">{story.abstractText}</p> : null}
      <div className="mt-4 space-y-1 text-xs text-slate-500">
        {story.byline ? <p>{story.byline}</p> : null}
        {story.publishedDate ? <p>{new Date(story.publishedDate).toLocaleString()}</p> : null}
      </div>
      <Link
        href={story.url}
        target="_blank"
        rel="noreferrer"
        className="mt-4 inline-flex text-sm font-medium text-blue-700 hover:text-blue-800"
      >
        Read story →
      </Link>
    </article>
  );
}
