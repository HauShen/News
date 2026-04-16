export interface CreateArticleRequest {
  title: string;
  content: string;
}

export interface UpdateArticleRequest {
  title: string;
  content: string;
}

export interface ArticleResponse {
  oid: number;
  title: string;
  content: string;
  createdAt: string;
  updatedAt?: string;
  likeCount: number;
  userId: string;
}

export interface ArticleUpdatedResponse {
  oid: number;
  title: string;
  content: string;
  createdAt: string;
  updatedAt?: string;
  likeCount: number;
  userId: string;
}
