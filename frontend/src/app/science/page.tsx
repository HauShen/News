"use client";

import { useEffect, useState } from "react";
import { ErrorMessage } from "@/components/ErrorMessage";
import { LoadingSpinner } from "@/components/LoadingSpinner";
import { NewsCard } from "@/components/NewsCard";
import { newsApi } from "@/lib/api";
import type { NewsStory } from "@/types";

export default function SciencePage() {
  const [stories, setStories] = useState<NewsStory[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    newsApi
      .getScience()
      .then(setStories)
      .catch((err: Error) => setError(err.message || "Unable to load science stories"))
      .finally(() => setLoading(false));
  }, []);

  return (
    <section className="space-y-6">
      <div>
        <h2 className="text-2xl font-semibold">Science News</h2>
        <p className="mt-1 text-sm text-slate-600">Curated science feed from NY Times</p>
      </div>
      {loading ? <LoadingSpinner /> : null}
      {error ? <ErrorMessage message={error} /> : null}
      {!loading && !error ? (
        <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
          {stories.map((story) => (
            <NewsCard key={`${story.title}-${story.url}`} story={story} />
          ))}
        </div>
      ) : null}
    </section>
  );
}
