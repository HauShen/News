import { useState } from 'react';
import { createArticle, getArticle, editArticle, deleteArticle } from '../api';

export default function ArticlesPage() {
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Create
  const [createUserId, setCreateUserId] = useState('');
  const [createTitle, setCreateTitle] = useState('');
  const [createContent, setCreateContent] = useState('');

  // Get / Delete
  const [oid, setOid] = useState('');

  // Edit
  const [editOid, setEditOid] = useState('');
  const [editTitle, setEditTitle] = useState('');
  const [editContent, setEditContent] = useState('');

  const clear = () => { setResult(null); setError(''); };

  async function handleCreate(e) {
    e.preventDefault();
    clear(); setLoading(true);
    try {
      const data = await createArticle(createUserId, { title: createTitle, content: createContent });
      setResult(data);
    } catch (err) { setError(err.message); }
    setLoading(false);
  }

  async function handleGet(e) {
    e.preventDefault();
    clear(); setLoading(true);
    try {
      const data = await getArticle(oid);
      setResult(data);
    } catch (err) { setError(err.message); }
    setLoading(false);
  }

  async function handleEdit(e) {
    e.preventDefault();
    clear(); setLoading(true);
    try {
      const data = await editArticle(editOid, { title: editTitle, content: editContent });
      setResult(data);
    } catch (err) { setError(err.message); }
    setLoading(false);
  }

  async function handleDelete(e) {
    e.preventDefault();
    clear(); setLoading(true);
    try {
      const data = await deleteArticle(oid);
      setResult(typeof data === 'string' ? { message: data } : data);
    } catch (err) { setError(err.message); }
    setLoading(false);
  }

  return (
    <div className="page">
      <h1>Articles</h1>

      <section className="form-section">
        <h2>Create Article</h2>
        <form onSubmit={handleCreate}>
          <input placeholder="Author User ID" value={createUserId} onChange={e => setCreateUserId(e.target.value)} required />
          <input placeholder="Title" value={createTitle} onChange={e => setCreateTitle(e.target.value)} required />
          <textarea placeholder="Content" value={createContent} onChange={e => setCreateContent(e.target.value)} rows={3} required />
          <button type="submit" disabled={loading}>Create</button>
        </form>
      </section>

      <section className="form-section">
        <h2>Get / Delete Article</h2>
        <form onSubmit={handleGet}>
          <input placeholder="Article OID" type="number" value={oid} onChange={e => setOid(e.target.value)} required />
          <button type="submit" disabled={loading}>Get</button>
          <button type="button" onClick={handleDelete} disabled={loading} className="btn-danger">Delete</button>
        </form>
      </section>

      <section className="form-section">
        <h2>Edit Article</h2>
        <form onSubmit={handleEdit}>
          <input placeholder="Article OID" type="number" value={editOid} onChange={e => setEditOid(e.target.value)} required />
          <input placeholder="Title" value={editTitle} onChange={e => setEditTitle(e.target.value)} required />
          <textarea placeholder="Content" value={editContent} onChange={e => setEditContent(e.target.value)} rows={3} required />
          <button type="submit" disabled={loading}>Edit</button>
        </form>
      </section>

      {error && <div className="error-box">{error}</div>}
      {result && (
        <div className="result-box">
          <h3>Result</h3>
          <pre>{JSON.stringify(result, null, 2)}</pre>
        </div>
      )}
    </div>
  );
}
