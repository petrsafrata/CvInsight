# 🚀 CV Insight — AI-Powered CV Processing REST API

![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Security-Spring_Security-6DB33F?logo=springsecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?logo=postgresql&logoColor=white)
![OpenAI](https://img.shields.io/badge/AI-OpenAI_Responses_API-412991?logo=openai&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![API](https://img.shields.io/badge/API-OpenAPI-85EA2D?logo=swagger&logoColor=black)
![License](https://img.shields.io/badge/License-Apache_2.0-blue?logo=apache&logoColor=white)

---

## 🧾 Project Description

**CV Insight** is a Java and Spring Boot REST API for processing candidate CVs, identifying suspicious document content, converting unstructured text into structured information, and comparing professional qualifications with job requirements.

The workflow separates conventional document processing from AI-assisted operations:

1. **Extract** readable text from PDF, DOC, and DOCX files without AI.
2. **Check** source documents for suspicious or hidden text and formatting without AI.
3. **Parse** extracted CV text into structured candidate data using the OpenAI Responses API.
4. **Store** and manage structured CV records in PostgreSQL.
5. **Analyze** a stored candidate profile against a job description using OpenAI.

> [!IMPORTANT]
> AI-generated results can be incomplete or inaccurate. Candidate analysis is intended to support human review, not replace human employment decisions.

---

## ✨ Application Features

### 🔐 Authentication and Security

- Username/password authentication with JWT.
- Access and refresh tokens, with separate token types and expiration checks.
- Protected application endpoints; login and token refresh are intended to be public.
- Centralized error handling and authentication responses.

### 📄 CV Text Extraction

- PDF, legacy DOC, and DOCX document support.
- Content-based media type detection with Apache Tika.
- No OpenAI request is made during text extraction.

### 🔎 Suspicious Content Inspection

- Detection of hidden text and unusually small fonts.
- Detection of white text where supported.
- Detection of text outside visible page boundaries in PDF files.
- Format-specific processing with Apache PDFBox and Apache POI.

> [!NOTE]
> Inspection capabilities vary across PDF, DOC, and DOCX. A clean result does not prove that a document is free of malicious or misleading content.

### 🤖 AI-Powered CV Parsing

- Converts extracted CV text into structured JSON using the OpenAI Responses API.
- Structured fields include contact information, location, languages and proficiency, driving licenses, work history, and education.
- Predefined trusted instructions and JSON schemas guide the response format.
- CV text is treated as untrusted input.

### 🗃️ CV Storage and Management

- Create, list, retrieve, and delete structured CV records.
- PostgreSQL persistence using Spring Data JPA.
- Structured child data for education, languages, and work experience.

### 🎯 Candidate Analysis

- Compares a stored CV with a supplied job description.
- Uses job-relevant candidate information, including languages, driving licenses, work history, and education.
- Excludes direct personal identifiers and personal characteristics from the candidate object sent for analysis.
- Produces a structured AI-generated assessment for human review.

### 📚 Additional Features

- Swagger UI / OpenAPI documentation.
- Application information endpoints: version, release notes, terms of use, and privacy policy.
- Log4j2-based application logging configuration.
- Development and production profiles.
- Docker/Compose support and automated tests.

---

## 🧱 Technology and Architecture

| Layer | Technologies |
|---|---|
| Backend | Java 17, Spring Boot 4.0.3, Spring Web MVC |
| Security | Spring Security, JWT (JJWT) |
| Data | PostgreSQL 16, Spring Data JPA, Flyway |
| Documents | Apache Tika, Apache POI, Apache PDFBox |
| AI | OpenAI Responses API, Spring WebClient, Jackson |
| Documentation | Springdoc OpenAPI, Swagger UI, Flexmark |
| Logging | SLF4J, Log4j2 |
| Testing | JUnit 5, Mockito, Testcontainers |
| Packaging | Maven, Docker, Docker Compose |

### 🧠 Processing Architecture

```text
Client / Swagger UI
        |
        v
 Spring Security (JWT)
        |
        v
 REST Controllers
        |
        +--> CV Extraction --> Apache Tika
        |
        +--> CV Inspection --> PDFBox / Apache POI
        |
        +--> CV Parsing ----> OpenAI Responses API
        |
        +--> CV Management -> Spring Data JPA -> PostgreSQL
        |
        +--> CV Analysis ---> Stored CV + Job Offer -> OpenAI Responses API
```

The API is a **backend-only project**; a browser frontend is not required.

---

## 🐳 Docker Services

The repository contains separate Compose configurations for development and production.

| Component | Purpose | Typical access |
|---|---|---|
| PostgreSQL | Persistent application database | Development: `localhost:5432`; production Compose mapping: `localhost:5431` |
| Backend | Spring Boot REST API | `localhost:8080` |

> [!NOTE]
> Actual service names, port mappings, image names, and environment variables are defined by `docker-compose.dev.yml` and `docker-compose.yml`. Check those files before deployment; there is no frontend container in CV Insight.

---

## 📁 Project Structure

```text
CvInsight/
├── src/
│   ├── main/
│   │   ├── java/cz/jpmad/cvinsight/
│   │   │   ├── api/             # REST controllers, API paths and DTOs
│   │   │   ├── config/          # Security, Jackson, OpenAPI and configuration
│   │   │   ├── exception/       # Application-specific exceptions
│   │   │   ├── model/           # Persistence entities and domain models
│   │   │   ├── repository/      # Spring Data repositories
│   │   │   └── service/         # Authentication, CV, AI and info services
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── prompts/         # Trusted AI instructions and JSON schemas
│   │       └── log4j2-spring.xml
│   └── test/java/cz/jpmad/cvinsight/
├── docker-compose.dev.yml
├── docker-compose.yml
├── Dockerfile
├── pom.xml
├── VERSION
├── README.md
└── CONTRIBUTING.md
```

Additional documentation files include `RELEASE-NOTES.md`, `TERMS_OF_USE.md`, and `PRIVACY_POLICY.md`; their runtime locations follow the resource paths configured in `InfoService`.

---

## ⚙️ Prerequisites

- **Java 17** for local development.
- **Maven** or the included Maven wrapper, if present.
- **Docker Desktop / Docker Engine with Compose** for containerized PostgreSQL and integration tests.
- An **OpenAI API key** for AI parsing and analysis.
- Available ports according to your selected Compose configuration (typically backend `8080`, database `5432` or `5431`).

> [!WARNING]
> Configure credentials locally before starting the application. Never commit `.env`, API keys, database passwords, or JWT signing secrets. If a credential was ever committed, rotate it even after cleaning Git history.

---

## 🛠️ Installation (Development and Production)

### 🧪 Development Mode

Clone the repository:

```powershell
git clone https://github.com/petrsafrata/CvInsight.git
cd CvInsight
```

Start the development database:

```powershell
docker compose -f docker-compose.dev.yml up -d
```

Configure local application properties. The project uses development and production profiles; the relevant settings are in `src/main/resources/application.yml` and `application-dev.yml`.

The application needs, at minimum, valid configuration for:

- PostgreSQL connection and credentials.
- JWT signing secret and token lifetimes.
- OpenAI API key, base URL, and model.
- Prompt/schema resources and document inspection thresholds.

Examples of the Spring configuration keys used by the services:

```yaml
security:
  jwt:
    secret: ${JWT_SECRET}
    accessTokenTtlSeconds: 3600
    refreshTokenTtlSeconds: 86400

openai:
  api-key: ${OPENAI_API_KEY}
  base-url: https://api.openai.com/v1
  model: ${OPENAI_MODEL}
```

**This is an illustrative fragment**, not a complete replacement for the project's configuration. Supply suitable local environment variables or follow the mappings already defined in the existing configuration files. Use a strong JWT secret; do not use sample secrets in production.

Run the backend locally:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

If Maven Wrapper is not present, use:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

### 🚀 Production Mode (Docker Compose)

Configure the variables expected by `docker-compose.yml`, then build and start the services:

```powershell
docker compose --env-file .env up -d --build
```

Inspect application logs:

```powershell
docker compose logs -f
```

Stop the deployment without deleting persistent database volumes:

```powershell
docker compose down
```

> [!IMPORTANT]
> The production deployment must provide valid database, JWT, and OpenAI configuration. Use HTTPS and restrict database exposure for deployments accessible beyond your development machine.

---

## 🌐 Access Points

| Access point | Typical URL | Description |
|---|---|---|
| Backend | `http://localhost:8080` | Spring Boot API host |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` | Interactive API documentation |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` | Machine-readable API specification |

> [!NOTE]
> If `server.servlet.context-path: /api` is configured, insert `/api` after the host in the URLs above (for example, `/api/swagger-ui/index.html`). Similarly, document endpoints under the configured context path and API version; use Swagger UI as the authoritative list of effective routes.

---

## 📚 API Endpoint Documentation

CV Insight groups its endpoints by authentication, CV document processing, stored CV management, candidate analysis, and application information.

| Operation | Method | Endpoint pattern |
|---|---|---|
| Login | `POST` | `/auth/login` |
| Current user | `GET` | `/auth/me` |
| Logout | `POST` | `/auth/logout` |
| Extract CV text | `POST` | `/v1/cv/extract` |
| Inspect CV | `POST` | `/v1/cv/check` |
| Parse CV text | `POST` | `/v1/cv/parse` |
| Create stored CV | `POST` | `/v1/cvs` |
| List stored CVs | `GET` | `/v1/cvs` |
| Retrieve stored CV | `GET` | `/v1/cvs/{id}` |
| Delete stored CV | `DELETE` | `/v1/cvs/{id}` |
| Analyze stored CV | `POST` | `/v1/cvs/{id}/analysis` |
| App version | `GET` | `/info/version` |
| Release notes | `GET` | `/info/release-notes` |
| Terms of use | `GET` | `/info/terms-of-service` |
| Privacy policy | `GET` | `/info/privacy-policy` |

These patterns reflect the service/controller structure and the versioned CV API used during development. The API version and an optional servlet context path are configurable, so verify final URLs in Swagger UI. The security configuration also allows a token-refresh route (`POST /auth/refresh`); availability depends on the authentication controller in the deployed revision.

### 🔑 Authentication Example

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"your-username","password":"your-password"}'
```

Use the returned access token when calling protected endpoints:

```bash
curl http://localhost:8080/auth/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

When a servlet context path is configured, adjust the examples accordingly. Use **Authorize** in Swagger UI to test authenticated operations interactively.

### Common HTTP Responses

| Status | Meaning |
|---|---|
| `200` | Successful request |
| `201` | Resource created |
| `204` | Successful request without content |
| `400` | Invalid request |
| `401` | Missing or invalid authentication |
| `403` | Access denied |
| `404` | Resource not found |
| `409` | Conflict |
| `415` | Unsupported file format |
| `429` | Rate limit exceeded |
| `500` | Unexpected server error |
| `502` | External AI response/request failure |
| `503` | External AI service unavailable |

---

## 🧪 Testing

Testing focuses on the service layer and database CRUD behavior.

- **Unit tests:** JUnit 5 and Mockito for JWT, document processing, service validation, AI response handling, and business logic.
- **Integration tests:** Testcontainers with PostgreSQL for persistence and application context checks.
- **External AI:** OpenAI requests should be mocked in automated tests rather than calling the real API.

Run the tests:

```powershell
mvn clean test
```

Run an individual test class:

```powershell
mvn "-Dtest=JwtServiceTest" test
```

Create a package:

```powershell
mvn clean package
```

> [!NOTE]
> Docker must be running for PostgreSQL Testcontainers integration tests. These tests should use isolated test credentials and must never target a production database.

---

## 🧯 Troubleshooting

### Backend fails to start

- Check application logs and the active Spring profile.
- Verify database URL, username, password, and container health.
- Verify that the configured JWT signing secret is sufficiently long.
- Verify that the OpenAI key, base URL, and model properties are present.

### PostgreSQL connection fails

- Check the correct Compose file and host port (`5432` in development or the configured production mapping).
- When connecting from another Docker container, use the Compose database service name instead of `localhost`.
- Check database container logs with `docker compose logs`.

### Login returns `401`

- Check the username and password.
- Check JWT validity, expiry, and signing configuration for protected requests.
- Use an **access token**, not a refresh token, in the Bearer authorization header.

### OpenAI requests fail

- Confirm API key, model, base URL, and available API access.
- HTTP `429` can indicate an upstream API rate limit.
- HTTP `502` indicates an external AI communication/response error; `503` indicates temporary unavailability.
- Check sanitized application logs; never log or paste real credentials or candidate personal data.

### SLF4J reports multiple providers

- Check Maven's dependency tree for Logback and Log4j2 bindings.
- The application should use a single SLF4J provider, consistent with `log4j2-spring.xml`.

### Unsupported or unreadable CV file

- Check that the file is a genuine PDF, DOC, or DOCX document.
- A file extension alone does not determine its detected format.
- Corrupt or encrypted documents may fail extraction or inspection.

---

## 📉 Current Limitations

- Supported document formats are limited to PDF, DOC, and DOCX.
- Suspicious-content detection is heuristic and differs by file format.
- AI parsing and analysis require OpenAI connectivity and may produce imperfect results.
- Candidate evaluation requires human oversight.
- The project exposes a REST API and Swagger UI, not a dedicated end-user web interface.
- Data retention, hosting security, and backup behavior depend on the deployment configuration.

---

## 🤝 Contributing

Contributions are welcome. Read [CONTRIBUTING.md](CONTRIBUTING.md) for coding conventions, security requirements, testing expectations, and pull request guidelines.

For details about application usage and personal data handling, see [TERMS_OF_USE.md](TERMS_OF_USE.md) and [PRIVACY_POLICY.md](PRIVACY_POLICY.md). Notable changes are tracked in [RELEASE-NOTES.md](RELEASE-NOTES.md).

---

## ⚖️ License

CV Insight is released under the **Apache License 2.0**. See [LICENSE](LICENSE) for the complete terms.

Apache-2.0 — Copyright (c) 2026 Petr Šafrata.
