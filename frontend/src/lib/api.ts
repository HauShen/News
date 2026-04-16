import type {
  ArticleResponse,
  ArticleUpdatedResponse,
  CreateArticleRequest,
  CreateUserRequest,
  FeedbackArticleResponse,
  FeedbackLikeResponse,
  NewsStory,
  SendWithFeedbackParams,
  UpdateArticleRequest,
  UpdateUserRequest,
  UserResponse,
} from "@/types";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

class ApiError extends Error {
  status: number;

  constructor(message: string, status: number) {
    super(message);
    this.name = "ApiError";
    this.status = status;
  }
}

async function apiRequest<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    headers: {
      "Content-Type": "application/json",
      ...(init?.headers ?? {}),
    },
    cache: "no-store",
  });

  if (!response.ok) {
    const text = await response.text();
    const message = text || `Request failed with status ${response.status}`;
    throw new ApiError(message, response.status);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  const contentType = response.headers.get("content-type") || "";
  if (contentType.includes("application/json")) {
    return (await response.json()) as T;
  }

  return (await response.text()) as T;
}

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
