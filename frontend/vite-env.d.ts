/// <reference types="vite/client" />

interface ImportMetaEnv {
  /** Base URL pro API požadavky (default: http://localhost:8080) */
  readonly VITE_API_URL: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
