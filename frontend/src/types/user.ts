export type UserRole = "NEWS_POSTER" | "READER" | "EMAIL_CREATOR";

export interface CreateUserRequest {
  name: string;
  age: number;
  role: UserRole;
}

export interface UpdateUserRequest {
  name: string;
  age: number;
  role: UserRole;
}

export interface UserResponse {
  userId: string;
  name: string;
  age: number;
  role: UserRole;
}
