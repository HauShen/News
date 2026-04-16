export interface FeedbackArticleResponse {
  userId: string;
  articleOid: number;
  articleTitle: string;
  articleContent: string;
  token: string;
}

export interface FeedbackLikeResponse {
  success: boolean;
  message: string;
  readerId: string;
  articleOid: number;
}
