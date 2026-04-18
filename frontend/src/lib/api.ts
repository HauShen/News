import type {
  ArticleResponse,
  ArticleUpdatedResponse,
  AuthResponse,
  CreateArticleRequest,
  CreateUserRequest,
  FeedbackArticleResponse,
  FeedbackLikeResponse,
  LoginRequest,
  NewsStory,
  SendWithFeedbackParams,
  SignupRequest,
  UpdateArticleRequest,
  UpdateUserRequest,
  UserResponse,
} from "@/types";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

const AUTH_STORAGE_KEY = "news_auth";

class ApiError extends Error {
  status: number;

  constructor(message: string, status: number) {
    super(message);
    this.name = "ApiError";
    this.status = status;
  }
}

function getStoredToken(): string | null {
  if (typeof window === "undefined") return null;
  try {
    const stored = localStorage.getItem(AUTH_STORAGE_KEY);
    if (stored) {
      const parsed = JSON.parse(stored) as AuthResponse;
      return parsed.token;
    }
  } catch {
    // ignore
  }
  return null;
}

async function apiRequest<T>(path: string, init?: RequestInit): Promise<T> {
  const token = getStoredToken();
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
    ...((init?.headers as Record<string, string>) ?? {}),
  };
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    headers,
    cache: "no-store",
  });

  if (!response.ok) {
    const text = await response.text();
    let message = text || `Request failed with status ${response.status}`;
    try {
      const json = JSON.parse(text);
      if (json.error) message = json.error;
    } catch {
      // use text as-is
    }
    throw new ApiError(message, response.status);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  const contentType = response.headers.get("content-type") || "";
  if (contentType.includes("application/json")) {
    const data = (await response.json()) as unknown;
    // TODO: Remove once backend standardizes `/article/edit/{oid}` response field from `oId` to `oid`.
    // Current normalization preserves compatibility while keeping frontend types consistent.
    if (data && typeof data === "object" && "oId" in data && !("oid" in data)) {
      (data as Record<string, unknown>).oid = (data as Record<string, unknown>).oId;
    }
    return data as T;
  }

  return (await response.text()) as T;
}

export const authApi = {
  signup: (body: SignupRequest) =>
    apiRequest<AuthResponse>("/auth/signup", {
      method: "POST",
      body: JSON.stringify(body),
    }),
  login: (body: LoginRequest) =>
    apiRequest<AuthResponse>("/auth/login", {
      method: "POST",
      body: JSON.stringify(body),
    }),
};

export const userApi = {
  create: (body: CreateUserRequest) =>
    apiRequest<UserResponse>("/user/create", {
      method: "POST",
      body: JSON.stringify(body),
    }),
  getById: (userId: string) => apiRequest<UserResponse>(`/user/get/${userId}`),
  update: (userId: string, body: UpdateUserRequest) =>
    apiRequest<UserResponse>(`/user/update/${userId}`, {
      method: "PUT",
      body: JSON.stringify(body),
    }),
  delete: (userId: string) =>
    apiRequest<string>(`/user/delete/${userId}`, {
      method: "DELETE",
    }),
};

export const articleApi = {
  create: (userId: string, body: CreateArticleRequest) =>
    apiRequest<ArticleResponse>(`/article/create/${userId}`, {
      method: "POST",
      body: JSON.stringify(body),
    }),
  getById: (id: string | number) => apiRequest<ArticleResponse>(`/article/get/${id}`),
  update: (id: string | number, body: UpdateArticleRequest) =>
    apiRequest<ArticleUpdatedResponse>(`/article/edit/${id}`, {
      method: "PUT",
      body: JSON.stringify(body),
    }),
  delete: (id: string | number) =>
    apiRequest<string>(`/article/delete/${id}`, {
      method: "DELETE",
    }),
};

export const newsApi = {
  getHome: () => apiRequest<NewsStory[]>("/api/nytimesnews/home"),
  getScience: () => apiRequest<NewsStory[]>("/api/nytimesnews/science"),
};

export const emailApi = {
  sendGmail: (emailAddress: string) =>
    apiRequest<string>(`/email/send_gmail/${encodeURIComponent(emailAddress)}`, {
      method: "POST",
    }),
  sendNyTimesNews: (emailAddress: string) =>
    apiRequest<string>(`/email/send_nytimes_news/${encodeURIComponent(emailAddress)}`),
  sendWithFeedback: ({ emailAddress, userId }: SendWithFeedbackParams) =>
    apiRequest<string>(
      `/email/send_with_feedback/${encodeURIComponent(emailAddress)}?userId=${encodeURIComponent(userId)}`,
      { method: "POST" },
    ),
};

export const feedbackApi = {
  getArticle: (token: string) =>
    apiRequest<FeedbackArticleResponse>(`/feedback/article?token=${encodeURIComponent(token)}`),
  submitLike: (token: string) =>
    apiRequest<FeedbackLikeResponse>(`/feedback/like?token=${encodeURIComponent(token)}`, {
      method: "POST",
    }),
};

export { ApiError, API_BASE_URL };
