import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App";
import { AuthProvider } from "./context/AuthContext";
import { authLogin } from "./api/api";
import type { LoginRequest } from "./api/types";

// Příklad: přizpůsobte podle typu autentizace
const handleLogin = async (request: LoginRequest) => {
    // Pro JWT: uložit token do localStorage
    // Pro Basic: poslat credentials
    // Pro OAuth: redirect na provider
    return await authLogin(request);
};

const handleLogout = async () => {
    // Pro JWT: smazat token z localStorage
    // Pro session: zavolat POST /api/auth/logout
    // localStorage.removeItem("token");
};

createRoot(document.getElementById("root")!).render(
  <StrictMode>
      <AuthProvider
          onLogin={handleLogin}
          onLogout={handleLogout}
          autoRefresh={true}
      >
          <App />
      </AuthProvider>
  </StrictMode>,
);
