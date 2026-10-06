import {
    createContext,
    useContext,
    useState,
    useEffect,
    useCallback,
    type ReactNode,
} from "react";
import type { AuthState, AuthContextValue } from "./AuthContextTypes";
import type { UserResponse } from "../api/types";
import { getMe } from "../api/api";

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

/**
 * Konfigurace pro AuthProvider.
 * Předejte vlastní implementace login/logout podle typu autentizace.
 */
export interface AuthProviderProps {
    children: ReactNode;
    /**
     * Vlastní login funkce. Měla by vrátit UserResponse při úspěchu.
     * Pokud není zadána, provider nemá login implementaci (vyhodí chybu).
     */
    onLogin?: (...args: any[]) => Promise<UserResponse>;
    /**
     * Vlastní logout funkce.
     * Pokud není zadána, pouze vymaže uživatele ze stanu.
     */
    onLogout?: () => Promise<void>;
    /**
     * Vlastní funkce pro načtení aktuálního uživatele.
     * Výchozí: volá getMe() z api.ts.
     */
    onRefreshUser?: () => Promise<UserResponse>;
    /**
     * Pokud false, neprovede se automatický refresh při mountu.
     * Výchozí: true
     */
    autoRefresh?: boolean;
}

export function AuthProvider({
                                 children,
                                 onLogin,
                                 onLogout,
                                 onRefreshUser,
                                 autoRefresh = true,
                             }: AuthProviderProps) {
    const [state, setState] = useState<AuthState>({
        user: null,
        loading: autoRefresh,
        initialized: false,
    });

    const refreshUser = useCallback(async () => {
        setState((prev) => ({ ...prev, loading: true }));
        try {
            const fetchUser = onRefreshUser ?? getMe;
            const user = await fetchUser();
            setState({ user, loading: false, initialized: true });
        } catch {
            setState({ user: null, loading: false, initialized: true });
        }
    }, [onRefreshUser]);

    // Automatický refresh session při mountu
    useEffect(() => {
        if (autoRefresh) {
            refreshUser();
        } else {
            setState((prev) => ({ ...prev, loading: false, initialized: true }));
        }
    }, [autoRefresh, refreshUser]);

    const login = useCallback(
        async (...args: any[]) => {
            if (!onLogin) {
                throw new Error(
                    "AuthProvider: onLogin není implementován. " +
                    "Předejte vlastní onLogin prop podle typu autentizace.",
                );
            }
            setState((prev) => ({ ...prev, loading: true }));
            try {
                const user = await onLogin(...args);
                setState({ user, loading: false, initialized: true });
            } catch (error) {
                setState((prev) => ({ ...prev, loading: false }));
                throw error;
            }
        },
        [onLogin],
    );

    const logout = useCallback(async () => {
        setState((prev) => ({ ...prev, loading: true }));
        try {
            if (onLogout) {
                await onLogout();
            }} finally {
            setState({ user: null, loading: false, initialized: true });
        }
    }, [onLogout]);

    return (
        <AuthContext.Provider
            value={{ ...state, login, logout, refreshUser }}
        >
            {children}
        </AuthContext.Provider>
    );
}

/**
 * Hook pro přístup k auth kontextu.
 * Musí být použit uvnitř <AuthProvider>.
 */
export function useAuth(): AuthContextValue {
    const ctx = useContext(AuthContext);
    if (!ctx) {
        throw new Error("useAuth musí být použit uvnitř <AuthProvider>");
    }
    return ctx;
}