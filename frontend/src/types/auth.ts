import type { UserRole } from "./user";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  name: string;
  email: string;
  password: string;
  age: number;
  role: UserRole;
}

export interface AuthResponse {
  token: string;
  userId: string;
  name: string;
  email: string;
  role: UserRole;
}
