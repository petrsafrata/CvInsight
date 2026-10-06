# Frontend -- React + TypeScript + Vite

Moderní šablona frontendu připravená pro komunikaci se Spring Boot
backendem. Projekt je postaven na **React + TypeScript + Vite** a
obsahuje základní API vrstvu, strukturu projektu a připravený Auth
kontext.

------------------------------------------------------------------------

## 🚀 Požadavky

-   **Node.js** ≥ 18
-   **npm** ≥ 9

Ověření verzí:

    node -v
    npm -v

------------------------------------------------------------------------

## ▶ Spuštění projektu

    # Instalace závislostí
    npm install

    # Spuštění vývojového serveru (http://localhost:5173)
    npm run dev

    # Produkční build
    npm run build

    # Náhled produkčního buildu
    npm run preview

------------------------------------------------------------------------

## 🌐 Konfigurace API URL

API URL se nastavuje přes proměnnou prostředí `VITE_API_URL`.

### 1. Vytvoření .env souboru

    cp .env.example .env

### 2. Nastavení URL backendu

    VITE_API_URL=http://localhost:8080

### Výchozí chování při lokálním vývoji

Vite dev server automaticky proxuje požadavky na `/api/*` na
`http://localhost:8080` (viz `vite.config.ts`), takže pro lokální vývoj
není nutné `.env` nastavovat.

------------------------------------------------------------------------

# 🎨 Instalace TailwindCSS (volitelné)

TailwindCSS není ve výchozí šabloně předinstalován, ale lze jej snadno
doplnit.

## 1️⃣ Instalace balíčků

    npm install -D tailwindcss postcss autoprefixer

## 2️⃣ Vygenerování konfigurace

    npx tailwindcss init -p

Tím se vytvoří: - `tailwind.config.js` - `postcss.config.js`

## 3️⃣ Úprava tailwind.config.js

    /** @type {import('tailwindcss').Config} */
    export default {
      content: [
        "./index.html",
        "./src/**/*.{js,ts,jsx,tsx}",
      ],
      theme: {
        extend: {},
      },
      plugins: [],
    }

⚠️ Důležité: cesta `./src/**/*.{ts,tsx}` musí být uvedena, jinak
Tailwind nebude generovat styly.

## 4️⃣ Přidání Tailwind direktiv do CSS

Do souboru `src/index.css` přidejte:

    @tailwind base;
    @tailwind components;
    @tailwind utilities;

## 5️⃣ Import CSS do aplikace

V `main.tsx`:

    import "./index.css";

## 6️⃣ Ověření

    <h1 className="text-3xl font-bold text-blue-500">
      Tailwind funguje 🎉
    </h1>

------------------------------------------------------------------------

# 📂 Struktura projektu

    frontend/
    ├── src/
    │   ├── api/                # API vrstva
    │   │   ├── client.ts       # Axios instance + interceptory
    │   │   ├── types.ts        # TypeScript typy (odpovídají backend DTOs)
    │   │   ├── authService.ts  # Auth funkce (login, register, me)
    │   │   └── index.ts        # Reexporty
    │   ├── App.tsx
    │   └── main.tsx
    ├── index.html
    ├── vite.config.ts
    ├── tsconfig.json
    ├── .env.example
    └── package.json

------------------------------------------------------------------------

# 🔌 API klient

Použití odkudkoliv v aplikaci:

    import { apiClient, authLogin, getMe, ApiError } from "@/api";

    const user = await authLogin({ username: "admin", password: "secret" });
    const me = await getMe();
    const { data } = await apiClient.get("/api/info/version");

Chyby z backendu jsou automaticky transformovány na `ApiError` s HTTP
statusem a strukturou z `GlobalExceptionHandler`.

------------------------------------------------------------------------

# 🐳 Docker

Projekt obsahuje `Dockerfile`, který:

-   vytvoří produkční build (`npm run build`)
-   nasadí aplikaci do Apache kontejneru

------------------------------------------------------------------------

# 📌 Doporučení

-   Upravte `AuthContext` dle typu autentizace (HTTP Basic / JWT).
-   Přizpůsobte CORS nastavení backendu.
-   Před nasazením do produkce vždy zkontrolujte `.env` konfiguraci.
