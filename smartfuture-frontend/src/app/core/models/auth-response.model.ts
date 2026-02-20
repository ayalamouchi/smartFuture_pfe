import { UserRole } from './user.model';

export interface AuthResponse {
  token: string;
  refreshToken?: string;
  role: UserRole;
  email: string;
  firstName?: string;
  lastName?: string;
  emailVerified: boolean;
  message: string;
}

export interface OtpSentResponse {
  message: string;
  email: string;
  expirationMinutes: number;
  success: boolean;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
  error?: string;
}
