import { useState } from 'react';
import { getNYTimesHome, getNYTimesScience } from '../api';

export default function NYTimesPage() {
  const [articles, setArticles] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [section, setSection] = useState('');

  async function fetchNews(fetcher, label) {
    setError('');
    setLoading(true);
    setSection(label);
    try {
      const data = await fetcher();
      setArticles(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message);
      setArticles([]);
    }
    setLoading(false);
  }

  return (
    <div className="page">
      <h1>NY Times Top Stories</h1>
      <div className="button-group">
        <button onClick={() => fetchNews(getNYTimesHome, 'Home')} disabled={loading}>
          🏠 Home Stories
        </button>
        <button onClick={() => fetchNews(getNYTimesScience, 'Science')} disabled={loading}>
          🔬 Science Stories
        </button>
      </div>

      {loading && <p className="loading">Loading {section} stories…</p>}
      {error && <div className="error-box">{error}</div>}

      {articles.length > 0 && (
        <div className="news-list">
          <h2>{section} — {articles.length} stories</h2>
          {articles.map((a, i) => (
            <div key={i} className="news-card">
              <h3>{a.title}</h3>
              {a.abstractText && <p>{a.abstractText}</p>}
              {a.url && (
                <a href={a.url} target="_blank" rel="noopener noreferrer">
                  Read on NYTimes ↗
                </a>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
