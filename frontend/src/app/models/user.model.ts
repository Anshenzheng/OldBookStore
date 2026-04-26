export interface User {
  id: number;
  username: string;
  nickname: string;
  email?: string;
  phone?: string;
  avatar?: string;
  role: string;
  status: string;
  createdAt: Date;
  lastLoginAt?: Date;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface SignUpRequest {
  username: string;
  email: string;
  password: string;
  nickname: string;
  phone?: string;
}

export interface JwtResponse {
  accessToken: string;
  tokenType: string;
  userId: number;
  username: string;
  nickname: string;
  role: string;
}

export interface ApiResponse {
  success: boolean;
  message: string;
}
