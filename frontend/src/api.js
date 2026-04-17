const API_BASE = '';

async function request(url, options = {}) {
  const res = await fetch(`${API_BASE}${url}`, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options,
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || res.statusText);
  }
  const contentType = res.headers.get('content-type');
  if (contentType && contentType.includes('application/json')) {
    return res.json();
  }
  return res.text();
}

// ---- Users ----
export function createUser(body) {
  return request('/user/create', { method: 'POST', body: JSON.stringify(body) });
}

export function getUser(userId) {
  return request(`/user/get/${userId}`);
}

export function updateUser(userId, body) {
  return request(`/user/update/${userId}`, { method: 'PUT', body: JSON.stringify(body) });
}

export function deleteUser(userId) {
  return request(`/user/delete/${userId}`, { method: 'DELETE' });
}

// ---- Articles ----
export function createArticle(userId, body) {
  return request(`/article/create/${userId}`, { method: 'POST', body: JSON.stringify(body) });
}

export function getArticle(oid) {
  return request(`/article/get/${oid}`);
}

export function editArticle(oid, body) {
  return request(`/article/edit/${oid}`, { method: 'PUT', body: JSON.stringify(body) });
}

export function deleteArticle(oid) {
  return request(`/article/delete/${oid}`, { method: 'DELETE' });
}

// ---- NY Times News ----
export function getNYTimesHome() {
  return request('/api/nytimesnews/home');
}

export function getNYTimesScience() {
  return request('/api/nytimesnews/science');
}
