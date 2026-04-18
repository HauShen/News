export type UserRole = "ADMIN" | "NEWS_POSTER" | "READER";

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
  email: string;
  age: number;
  role: UserRole;
}
