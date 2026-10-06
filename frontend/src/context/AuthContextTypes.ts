import type { UserResponse } from "../api/types";

export interface AuthState {
    /** Přihlášený uživatel nebo null */
    user: UserResponse | null;
    /** Probíhá načítání (login, refresh…) */
    loading: boolean;
    /** Proběhl první pokus o ověření session */
    initialized: boolean;
}

export interface AuthContextValue extends AuthState {
    /**
     * Přihlášení uživatele.
     * Implementaci přizpůsobte typu autentizace (Basic, JWT, OAuth…).
     */
    login: (...args: any[]) => Promise<void>;
    /** Odhlášení uživatele */
    logout: () => Promise<void>;
    /** Znovu načte aktuálního uživatele (např. po refreshi stránky) */
    refreshUser: () => Promise<void>;
}