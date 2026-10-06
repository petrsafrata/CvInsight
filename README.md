# 🚀 Spring Boot + React Template

![Spring Boot](https://img.shields.io/badge/Backend-Spring_Boot-brightgreen.svg?logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Language-Java_17-blue.svg?logo=java)
![React](https://img.shields.io/badge/Frontend-React-61DAFB.svg?logo=react&logoColor=white)
![TypeScript](https://img.shields.io/badge/Language-TypeScript_5.0-3178C6.svg?logo=typescript&logoColor=white)
![Vite](https://img.shields.io/badge/Build-Vite-646CFF.svg?logo=vite&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-336791.svg?logo=postgresql)
![Docker](https://img.shields.io/badge/Container-Docker_Compose-2496ED.svg?logo=docker)
![Status](https://img.shields.io/badge/Project-Template-orange.svg?logo=lock)
![Build](https://github.com/petrsafrata/Spring-project-template/actions/workflows/ci-release.yml/badge.svg)

Předkonfigurovaná šablona pro vývoj fullstack aplikací postavených na **Spring Boot (Backend API)** a **React (Frontend SPA)**.

Cílem šablony je urychlit vývoj nových projektů tím, že poskytuje připravenou základní architekturu, bezpečnost, konfiguraci, CI workflow a kontejnerizaci.

---

# 📦 Obsah projektu

Projekt je rozdělen na dvě hlavní části:

- **Backend (BA)** – Spring Boot REST API
- **Frontend (FA)** – React aplikace (Vite + TypeScript)

Součástí jsou také:
- Docker konfigurace
- GitHub Actions workflow
- Automatické verzování a release systém

---

## 📁 Struktura projektu

```
Spring-project-template/
├── src/                 # zdrojové kody Spring Boot backendu
├── frontend/            # frontend soubory včetně konfigurací a vite
├── .env
├── .gitignore
├── docker-compose.dev.yml
├── docker-compose.yml
├── Dockerfile
├── LICENSE
├── pom.xml
├── README.md
├── update-version.bat
└── VERSION
```
---

# 🧠 Backend (Spring Boot)

Backend obsahuje předpřipravenou architekturu a základní implementace:

## 🔐 Autentizace a uživatelé

- Základní entita `User`
- `UserRepository`
- `UserService` implementující `UserDetailsService` pro Spring Security
- Základní API pro:
    - registraci
    - přihlášení
- Konfigurovaná autentizace přes Spring Security
- Vlastní `PasswordEncoder` bean

## ℹ️ Info Service

Obsahuje `InfoService`, který načítá informace o aplikaci z classpath souborů:

- Terms of Service
- Privacy Policy
- Release Notes
- Version

Tyto údaje jsou načítány ze souborů umístěných v `src/main/resources`.

## 🌍 API

- Auth API
- Info API
- Global Exception Handler

## ⚙️ Konfigurace

Projekt obsahuje:

### `SecurityConfig`
- Řeší autentizaci
- Definuje povolené endpointy
- Konfiguruje autorizaci podle rolí

### `WebConfig`
- Nastavení CORS pro komunikaci s frontendem

### `PasswordEncoder` bean
- Samostatně konfigurovaný encoder pro bezpečné hashování hesel

---

# 🗂 Konfigurační soubory

## `application.yml`

Určen pro **produkční nasazení**.

- Konfigurační hodnoty (DB, server atd.) se načítají z `.env` souboru

>[!IMPORANT]
>Doporučeno přidat `.env` do `.gitignore`

---

## `application.dev.yml`

Určen pro **lokální vývoj**.

- Obsahuje connection string na `testdb`
- Databáze je definována v `docker-compose.dev.yml`
- Možnost přidat volumes pro persistenci
- Používá PostgreSQL

---

# 🐳 Docker

## Backend

- Obsahuje `Dockerfile`
- Lze sestavit jako samostatný kontejner

## Frontend

- Vlastní `Dockerfile`
- Sestavení React aplikace
- Spuštění pomocí Apache v kontejneru

## Docker Compose
- `docker-compose.dev.yml` pro lokální vývoj
- Definuje službu PostgreSQL + případné další

| Služba | Popis               | Kontejner     | Porty     | Volume | Závislosti |
|--------|---------------------|---------------|-----------|--------|------------|
| `db`   | Databáze PostgreSQL | `db-postgres` | 5432:5432 | -      | –          |

>[!TIP]
> Spuštění: `docker-compose -f docker-compose.dev.yml up --build`

- `docker-compose.dev` je připraven pro produkční nasazení a spuštění služeb, nutné definovat

---

# ⚛️ Frontend (React + Vite + TypeScript)

Frontend obsahuje:

## 🌐 API Client

- Axios klient pro komunikaci s backendem
- Předpřipravené metody pro volání backend endpointů

## 📦 Struktura

- Připravené DTO definice
- `errorManager`
- Základní `AuthContext`

> ⚠️ `AuthContext` je nutné upravit dle konkrétního typu autentizace (HTTP Basic, JWT apod.)
> Stejně tak je třeba upravit `SecurityConfig.java` dle požadovaného auth mechanismu.

## 🎨 Styling

- TailwindCSS není předinstalován
- Lze jej jednoduše doplnit dle potřeby

>[!NOTE]
>Více informací: [frontend/README.md](frontend/README.md)


---

# 🔄 GitHub Actions Workflow

Projekt obsahuje připravené workflow, které:

1. Při push do `master`:
    - sestaví backend (s PostgreSQL service v CI)
    - sestaví frontend
2. Pokud build projde:
    - zkontroluje změnu souboru `VERSION`
    - pokud byl změněn:
        - vytvoří git tag
        - vytvoří GitHub Release

> [!NOTE]
> Workflow může být potřeba upravit dle konkrétní konfigurace databáze nebo testů.

---

# 🏷 Verzování

## Soubor `VERSION`

Obsahuje aktuální verzi projektu (SemVer: `X.Y.Z`).

## `update-version.bat`

Skript, který:

- Aktualizuje verzi v:
    - souboru `VERSION`
    - `pom.xml`
- Vytvoří git tag

---

# 🧩 Technologie a architektura

## 🧱 Technologie a architektura
- **Backend**: Java 17, Spring Boot, Spring Security, Spring Data JPA, Actuator, Swagger UI
- **Frontend**: Apache, React, Vite, TypeScript
- **Databáze**: PostgreSQL
- **Containerizace**: Docker, Docker Compose

---

# 📌 Doporučení při použití šablony

- Projít všechny konfigurační soubory
- Upravit:
    - autentizační logiku
    - CORS pravidla
    - role a autorizaci
    - konfiguraci DB
- Zvážit:
    - přidání TailwindCSS
    - doplnění testů
    - úpravu CI workflow dle potřeby

---

# 🎯 Účel šablony

Tato šablona slouží jako základ pro nové projekty.  
Jejím cílem není být finální aplikací, ale robustním startovacím bodem s:

- odděleným backendem a frontendem
- připravenou bezpečností
- kontejnerizací
- automatizovaným buildem a release procesem

---
> [!WARNING]
> ⚠️ Před použitím v produkci vždy proveď důkladnou kontrolu bezpečnostního nastavení a konfigurací.

