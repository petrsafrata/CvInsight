/**
 * DTO typy – odpovídají backend DTOs.
 */

/** POST /api/auth/login */
export interface LoginRequest {
  username: string;
  password: string;
}

/** POST /api/auth/register */
export interface RegisterRequest {
  username: string;
  password: string;
}

/** Response z auth endpointů */
export interface UserResponse {
  id: string;
  username: string;
  role: string;
}

/** Struktura chybové odpovědi z backendu (GlobalExceptionHandler) */
export interface ApiErrorBody {
  timestamp: string;
  code: string;
  message: string;
  details?: string;
}

/** Response z /api/version */
export interface VersuionResponse {
  version: string;
}

/** Response z /api/release-notes, /api/terms-of-service, /api/privacy-policy */
export interface HtmlResponse {
    html: string;
}