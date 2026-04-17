export default function HomePage() {
  return (
    <div className="page">
      <h1>Welcome to the News App</h1>
      <p>A full-stack Spring Boot + React application for managing users, articles, and reading NY Times news.</p>

      <div className="card-grid">
        <div className="card">
          <h2>👤 Users</h2>
          <p>Create, view, update and delete users with roles like NEWS_POSTER, READER, and EMAIL_CREATOR.</p>
        </div>
        <div className="card">
          <h2>📝 Articles</h2>
          <p>Manage news articles — create, read, edit, and delete. Each article is linked to a user.</p>
        </div>
        <div className="card">
          <h2>🗞️ NY Times</h2>
          <p>Browse top stories and science news from the New York Times API in real time.</p>
        </div>
      </div>
    </div>
  );
}
