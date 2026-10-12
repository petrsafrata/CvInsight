# Release Notes

All notable changes to **CV Insight REST API** are documented in this file.

---

## [1.0.0] - 2026-10-11

### Added

- Initial stable release of CV Insight REST API.
- JWT-based user authentication with access and refresh tokens.
- Authentication endpoints for obtaining and refreshing JWT tokens.
- Text extraction from PDF, DOC, and DOCX documents using Apache Tika.
- Document inspection for suspicious or hidden CV content.
- Detection of hidden text, unusually small fonts, and white text in supported document formats.
- Detection of text positioned outside visible page boundaries in PDF documents.
- AI-powered CV parsing using the OpenAI Responses API.
- Structured JSON output for extracted candidate information.
- Extraction of candidate contact details, language proficiency, driving licenses, work experience, and education.
- PostgreSQL persistence for structured CV records.
- REST endpoints for creating, listing, retrieving, and deleting stored CV records.
- AI-powered candidate analysis against job descriptions.
- Candidate qualification analysis based on professional experience, education, languages, and driving licenses.
- Application information endpoints for version details, release notes, terms of use, and privacy policy.
- Database schema management using Flyway.
- Docker-based application deployment.
- Docker Compose configurations for development and production environments.
- OpenAPI specification and interactive Swagger UI documentation.
- Unit tests for core application services.
- PostgreSQL integration tests using Testcontainers.

### Changed

- Adapted the original Spring project template for the CV Insight application.
- Organized the application into dedicated authentication, CV processing, persistence, and AI service components.
- Introduced separate development and production configuration profiles.
- Centralized configurable parameters for supported file formats and document inspection thresholds.
- Standardized API responses and exception handling.
- Improved logging across document processing, authentication, persistence, and AI operations.
- Updated API documentation to reflect the completed application functionality.

### Fixed

- Fixed handling of missing font-size values in DOCX documents.
- Improved validation of empty and unsupported document uploads.
- Corrected OpenAI Responses API request formatting for structured JSON output.
- Fixed JSON serialization and deserialization compatibility issues between Jackson and Spring WebClient.
- Improved validation of empty or malformed external AI responses.
- Improved handling of missing CV records and invalid request data.
- Resolved conflicting SLF4J logging provider configuration.

### Security

- Added JWT authentication for protected API endpoints.
- Implemented access token and refresh token type validation.
- Added token expiration and signature verification.
- Introduced request validation and centralized authentication error handling.
- Added safeguards against prompt injection attempts embedded in CV documents.
- Separated trusted AI instructions from untrusted candidate input.
- Defined structured JSON schemas for AI responses.
- Restricted AI candidate analysis input to job-relevant professional data.
- Excluded direct personal identifiers and protected personal characteristics from candidate analysis input.
- Improved error handling for OpenAI rate limits and temporary service unavailability.
- Added support for environment-based configuration of application credentials and secrets.

### Documentation

- Added comprehensive Swagger / OpenAPI documentation.
- Documented authentication and authorization requirements.
- Documented the CV processing workflow.
- Documented supported document formats and inspection capabilities.
- Documented AI parsing and candidate analysis functionality.
- Documented common HTTP response codes and error handling.
- Added Terms of Use.
- Added Privacy Policy.
- Added release notes for version 1.0.0.