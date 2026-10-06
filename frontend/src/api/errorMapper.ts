import { ApiError } from "./api";

/**
 * Mapa backend error kódů → uživatelsky srozumitelné zprávy.
 * Rozšiřuj dle potřeby projektu.
 */
const ERROR_MESSAGES: Record<string, string> = {
    UNAUTHORIZED: "Nesprávné přihlašovací údaje.",
    FORBIDDEN: "Na tuto akci nemáte oprávnění.",
    NOT_FOUND: "Požadovaný záznam nebyl nalezen.",
    CONFLICT: "Záznam již existuje.",
    VALIDATION_ERROR: "Neplatná data. Zkontrolujte vyplněné údaje.",
    INTERNAL_SERVER_ERROR: "Došlo k neočekávané chybě. Zkuste to později.",
    NETWORK_ERROR: "Nepodařilo se spojit se serverem. Zkontrolujte připojení k internetu.",
};

const STATUS_MESSAGES: Record<number, string> = {
    400: "Neplatný požadavek.",
    401: "Nesprávné přihlašovací údaje.",
    403: "Na tuto akci nemáte oprávnění.",
    404: "Požadovaný záznam nebyl nalezen.",
    409: "Záznam již existuje.",
    500: "Došlo k neočekávané chybě. Zkuste to později.",
};

const DEFAULT_MESSAGE = "Došlo k neočekávané chybě.";

/**
 * Přeloží ApiError na srozumitelnou zprávu pro uživatele.
 * Nikdy nevracíe technické detaily (stack trace apod.).
 */
export function mapErrorToMessage(error: unknown): string {
    if (error instanceof ApiError) {
        return (
            ERROR_MESSAGES[error.code] ??
            STATUS_MESSAGES[error.status] ??
            DEFAULT_MESSAGE
        );
    }

    if (error instanceof Error) {
        return DEFAULT_MESSAGE;
    }

    return DEFAULT_MESSAGE;
}