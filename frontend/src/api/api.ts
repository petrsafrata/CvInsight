import axios from "axios";
import type {
    AxiosInstance,
    InternalAxiosRequestConfig,
    AxiosResponse,
    AxiosError,
} from "axios";
import type {
    LoginRequest,
    RegisterRequest,
    UserResponse, ApiErrorBody, VersuionResponse, HtmlResponse,
} from "./types";

// ─── ApiError ────────────────────────────────────────────────

export class ApiError extends Error {
    public readonly status: number;
    public readonly code: string;
    public readonly body: ApiErrorBody | null;

    constructor(
        status: number,
        code: string,
        message: string,
        body: ApiErrorBody | null = null,
    ) {
        super(message);
        this.name = "ApiError";
        this.status = status;
        this.code = code;
        this.body = body;
    }
}

// ─── Axios instance ──────────────────────────────────────────

const apiClient: AxiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080",
    timeout: 15_000,
    headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
    },
    withCredentials: true,
});

// Request interceptor
apiClient.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        // Místo pro JWT token:
        // const token = localStorage.getItem("token");
        // if (token) config.headers.Authorization = `Bearer ${token}`;
        return config;
    },
    (error: AxiosError) => Promise.reject(error),
);

// Response interceptor
apiClient.interceptors.response.use(
    (response: AxiosResponse) => response,
    (error: AxiosError<ApiErrorBody>) => {
        if (error.response) {
            const { status, data } = error.response;
            const message = data?.message ?? error.message;
            const code = data?.code ?? String(status);

            console.error(`[API ${status}] ${code}: ${message}`);
            return Promise.reject(new ApiError(status, code, message, data ?? null));
        }

        console.error("[API] Network error:", error.message);
        return Promise.reject(new ApiError(0, "NETWORK_ERROR", error.message));
    },
);

// ─── Auth service funkce ─────────────────────────────────────

/** POST /api/auth/login */
export async function authLogin(request: LoginRequest): Promise<UserResponse> {
    const { data } = await apiClient.post<UserResponse>(
        "/api/auth/login",
        request,
    );
    return data;
}

/** POST /api/auth/register */
export async function authRegister(
    request: RegisterRequest,
): Promise<UserResponse> {
    const { data } = await apiClient.post<UserResponse>(
        "/api/auth/register",
        request,
    );
    return data;
}

/** GET /api/auth/me */
export async function getMe(): Promise<UserResponse> {
    const { data } = await apiClient.get<UserResponse>("/api/auth/me");
    return data;
}

/** POST /api/auth/me */
export async function authLogout(): Promise<void> {
    await apiClient.post("/api/auth/logout");
}

// ─── Info service funkce ─────────────────────────────────────


/** GET /api/version */
export async function getAppVersion(): Promise<VersuionResponse> {
    const { data } = await apiClient.get<VersuionResponse>("/api/version");
    return data;
}

/** GET /api/release-notes */
export async function getAppReleaseNotes(): Promise<HtmlResponse> {
    const { data } = await apiClient.get<HtmlResponse>("/api/release-notes");
    return data;
}

/** GET /api/terms-of-service */
export async function getAppTermsOfService(): Promise<HtmlResponse> {
    const { data } = await apiClient.get<HtmlResponse>("/api/terms-of-service");
    return data;
}

/** GET /api/privacy-policy */
export async function getAppPrivacyPolicy(): Promise<HtmlResponse> {
    const { data } = await apiClient.get<HtmlResponse>("/api/privacy-policy");
    return data;
}

export { apiClient };