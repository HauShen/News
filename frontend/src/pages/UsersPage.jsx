import { useState } from 'react';
import { createUser, getUser, updateUser, deleteUser } from '../api';

export default function UsersPage() {
  const [userId, setUserId] = useState('');
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Create form
  const [name, setName] = useState('');
  const [age, setAge] = useState('');
  const [role, setRole] = useState('READER');

  // Edit form
  const [editName, setEditName] = useState('');
  const [editAge, setEditAge] = useState('');
  const [editRole, setEditRole] = useState('READER');
  const [editUserId, setEditUserId] = useState('');

  const clear = () => { setResult(null); setError(''); };

  async function handleCreate(e) {
    e.preventDefault();
    clear(); setLoading(true);
    try {
      const data = await createUser({ name, age: parseInt(age, 10), role });
      setResult(data);
    } catch (err) { setError(err.message); }
    setLoading(false);
  }

  async function handleGet(e) {
    e.preventDefault();
    clear(); setLoading(true);
    try {
      const data = await getUser(userId);
      setResult(data);
    } catch (err) { setError(err.message); }
    setLoading(false);
  }

  async function handleUpdate(e) {
    e.preventDefault();
    clear(); setLoading(true);
    try {
      const data = await updateUser(editUserId, { name: editName, age: parseInt(editAge, 10), role: editRole });
      setResult(data);
    } catch (err) { setError(err.message); }
    setLoading(false);
  }

  async function handleDelete(e) {
    e.preventDefault();
    clear(); setLoading(true);
    try {
      const data = await deleteUser(userId);
      setResult(typeof data === 'string' ? { message: data } : data);
    } catch (err) { setError(err.message); }
    setLoading(false);
  }

  return (
    <div className="page">
      <h1>Users</h1>

      <section className="form-section">
        <h2>Create User</h2>
        <form onSubmit={handleCreate}>
          <input placeholder="Name" value={name} onChange={e => setName(e.target.value)} required />
          <input placeholder="Age" type="number" value={age} onChange={e => setAge(e.target.value)} required />
          <select value={role} onChange={e => setRole(e.target.value)}>
            <option value="NEWS_POSTER">NEWS_POSTER</option>
            <option value="READER">READER</option>
            <option value="EMAIL_CREATOR">EMAIL_CREATOR</option>
          </select>
          <button type="submit" disabled={loading}>Create</button>
        </form>
      </section>

      <section className="form-section">
        <h2>Get / Delete User</h2>
        <form onSubmit={handleGet}>
          <input placeholder="User ID" value={userId} onChange={e => setUserId(e.target.value)} required />
          <button type="submit" disabled={loading}>Get</button>
          <button type="button" onClick={handleDelete} disabled={loading} className="btn-danger">Delete</button>
        </form>
      </section>

      <section className="form-section">
        <h2>Update User</h2>
        <form onSubmit={handleUpdate}>
          <input placeholder="User ID" value={editUserId} onChange={e => setEditUserId(e.target.value)} required />
          <input placeholder="Name" value={editName} onChange={e => setEditName(e.target.value)} required />
          <input placeholder="Age" type="number" value={editAge} onChange={e => setEditAge(e.target.value)} required />
          <select value={editRole} onChange={e => setEditRole(e.target.value)}>
            <option value="NEWS_POSTER">NEWS_POSTER</option>
            <option value="READER">READER</option>
            <option value="EMAIL_CREATOR">EMAIL_CREATOR</option>
          </select>
          <button type="submit" disabled={loading}>Update</button>
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
