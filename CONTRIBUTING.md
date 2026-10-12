
# 🤝 Contributing to CV Insight

Welcome and thank you for taking the time to contribute.

CV Insight is a REST API for extracting, inspecting, parsing, storing, and analyzing candidate CVs. The project favors minimal, secure, maintainable, and well-tested changes that follow the existing application architecture.

For installation, configuration, development setup, and build instructions, see the [README](README.md).

---

## 📌 How to Contribute

1. Fork the repository.
2. Create a feature branch.
3. Implement your changes.
4. Build and test the application.
5. Submit a pull request.

Example branch names:

```text
feature/cv-format-support
feature/candidate-analysis
fix/pdf-text-detection
fix/jwt-validation
docs/api-documentation
test/cv-service-integration
```

In your pull request description, include:

- What changed
- Why the change is needed
- How the change was tested
- Any security or privacy impact
- Any database schema or configuration changes
- Any new dependency or external service requirement
- Any API contract changes

---

## 🧭 Contribution Rules

### Code Quality

- Keep changes minimal, localized, and consistent with the existing codebase.
- Use Java 17 and follow the established Spring Boot architecture.
- Prefer explicit and maintainable implementations over unnecessary abstractions.
- Follow existing package naming and code formatting conventions.
- Keep controllers, services, repositories, and configuration classes focused on their responsibilities.
- Use constructor injection for Spring-managed dependencies.
- Use Lombok where appropriate and consistent with the existing codebase.
- Avoid unrelated refactoring or formatting changes.
- Avoid new dependencies unless their purpose is clearly justified.
- Preserve existing API behavior unless the change intentionally modifies it.
- Handle exceptions consistently with the application's centralized exception handling.

### Application Architecture

CV Insight separates functionality into dedicated components:

- **Controllers** - handle HTTP requests, validation, and responses.
- **Services** - implement business logic and coordinate application operations.
- **Repositories** - provide database persistence through Spring Data JPA.
- **Models and DTOs** - represent persistent entities and API data structures.
- **Configuration** - manages security, integrations, and application settings.
- **AI Services** - communicate with OpenAI and process structured responses.
- **Document Checkers** - inspect supported CV formats for suspicious content.

When modifying the architecture:

- Preserve the separation between controllers, services, and repositories.
- Avoid placing business logic directly in controllers.
- Use DTOs for API requests and responses.
- Do not expose persistence entities unnecessarily.
- Keep configuration parameters externalized.
- Use `@Value` for configurable application parameters where appropriate.
- Preserve transactional boundaries for database operations.
- Follow the existing API endpoint naming and versioning conventions.

### REST API Compatibility

When changing REST endpoints:

- Follow standard HTTP methods and status codes.
- Validate incoming request data.
- Return structured and consistent API responses.
- Use the existing global exception handling mechanism.
- Preserve backward compatibility whenever possible.
- Document new endpoints and response models using OpenAPI annotations.
- Update Swagger documentation when API behavior changes.
- Do not expose sensitive internal implementation details through error messages.

Breaking changes should be clearly documented and justified.

### Document Processing

CV Insight supports PDF, DOC, and DOCX documents.

Document processing must follow these rules:

- Validate uploaded files before processing.
- Detect actual document formats rather than relying only on file extensions.
- Reject unsupported document types.
- Handle malformed and empty documents safely.
- Use Apache Tika, Apache POI, and Apache PDFBox consistently with their existing responsibilities.
- Ensure document resources and input streams are properly closed.
- Do not execute instructions or embedded content from uploaded documents.
- Avoid logging extracted CV text or unnecessary personal information.
- Preserve non-AI text extraction and document inspection functionality.

When modifying document checkers, consider:

- Hidden text
- Unusually small font sizes
- White or difficult-to-read text
- Text positioned outside visible page boundaries
- Differences between supported document formats

Detection capabilities may vary depending on the document format.

### AI Integration

CV Insight uses the OpenAI Responses API for structured CV parsing and candidate analysis.

AI-related contributions must follow these principles:

- Treat CV text and job descriptions as untrusted input.
- Keep trusted AI instructions separate from user-supplied content.
- Never allow instructions embedded in CV documents to override application rules.
- Use predefined JSON schemas for structured AI output.
- Validate AI responses before converting them into application DTOs.
- Handle missing, incomplete, and malformed responses safely.
- Do not fabricate missing candidate information.
- Avoid sending unnecessary personal information to external AI services.
- Do not introduce AI processing into endpoints designed to operate without AI.
- Handle API rate limits, connection failures, and temporary service unavailability appropriately.

Changes to AI prompts or JSON schemas should be reviewed together with the corresponding Java services and response models.

### Candidate Analysis and Fairness

Candidate analysis must focus on job-related qualifications.

Relevant information includes:

- Professional experience
- Education and qualifications
- Language proficiency
- Driving licenses
- Other legitimate job requirements

When changing candidate analysis:

- Do not introduce discrimination based on protected personal characteristics.
- Do not include unnecessary personal identifiers in AI analysis input.
- Preserve the separation between candidate professional data and personal information.
- Avoid unsupported assumptions about candidate suitability.
- Ensure analysis results can be reviewed by a human.

AI-generated analysis must not be treated as the sole basis for employment decisions.

### Database and Persistence

CV Insight uses PostgreSQL, Spring Data JPA, and Flyway.

When modifying persistent data:

- Use repositories for database access.
- Preserve existing entity relationships and data integrity.
- Use appropriate transactional boundaries.
- Avoid unnecessary database queries.
- Provide Flyway migrations for database schema changes.
- Do not modify existing applied migrations.
- Create new versioned migrations for subsequent schema changes.
- Consider compatibility with existing stored records.
- Verify that create, read, and delete operations continue to work.

Database-related changes should include appropriate integration tests.

---

## 🔐 Security

Security-sensitive changes must follow these rules:

- Never commit API keys, passwords, JWT secrets, access tokens, or credentials.
- Never log authorization headers or sensitive authentication information.
- Do not commit `.env` files containing real credentials.
- Use environment variables and externalized configuration for secrets.
- Keep sensitive configuration values out of source control.
- Never expose the OpenAI API key through REST responses.
- Preserve JWT authentication for protected endpoints.
- Validate JWT signatures, expiration, and token types.
- Validate untrusted input before processing.
- Do not expose complete CV content in application logs.
- Do not expose stack traces or internal secrets in API responses.
- Avoid introducing unnecessary network access or external integrations.
- Review third-party dependencies for security implications.

> [!IMPORTANT]
> Any change affecting authentication, JWT handling, credentials, uploaded documents, database access, AI processing, or network communication must include a short security explanation in the pull request.

### Environment Configuration

Local configuration files containing credentials must remain outside version control.

Use a local `.env` file or the supported application configuration mechanism.

If an example environment file is provided, it must contain only placeholder values.

Never include real secrets in:

- Source code
- Test fixtures
- Git commits
- GitHub Issues
- Pull requests
- Documentation
- Application logs

If credentials are accidentally exposed, revoke or rotate them and report the incident.

---

## 🔒 Privacy

CV Insight processes candidate information that may include personal data.

Privacy must be considered whenever changing document processing, storage, logging, or AI functionality.

Update `PRIVACY_POLICY.md` when a change affects:

- Categories of processed personal data
- CV extraction or parsing
- Candidate analysis
- External AI communication
- Database storage
- Data retention or deletion
- Authentication-related information
- Application logging
- Third-party integrations

When implementing new functionality:

- Process only the information necessary for the intended operation.
- Avoid collecting unnecessary personal information.
- Minimize personal data transmitted to external services.
- Preserve existing protections for candidate information.
- Avoid including sensitive data in exceptions and logs.
- Ensure stored CV records can be managed through authorized operations.

> [!IMPORTANT]
> Do not introduce analytics, telemetry, advertising, or additional personal data collection without prior discussion and appropriate documentation.

---

## 📚 Documentation

Update the relevant documentation when changing:

- REST API endpoints
- Request or response formats
- Authentication requirements
- Supported document formats
- AI parsing or analysis behavior
- Database configuration
- Environment variables
- Build or deployment instructions
- Application dependencies
- Security or privacy behavior

Relevant documentation includes:

- `README.md` - project overview, setup, and usage
- `CONTRIBUTING.md` - contribution guidelines
- `RELEASE-NOTES.md` - version history and notable changes
- `TERMS_OF_USE.md` - terms of use
- `PRIVACY_POLICY.md` - privacy and data processing information
- Swagger / OpenAPI - interactive API documentation

Keep documentation accurate, concise, and consistent with implemented functionality.

---

## 🧪 Testing

CV Insight uses:

- **JUnit 5** for unit and integration tests.
- **Mockito** for mocking dependencies.
- **Testcontainers** for PostgreSQL integration testing.

### Unit Tests

Unit tests should focus on relevant service behavior, including:

- Input validation
- Authentication and JWT handling
- Document processing
- AI response parsing
- Business logic
- Error handling

Use mocks for external dependencies where appropriate.

Do not make real OpenAI API requests in automated unit tests.

### Integration Tests

Integration tests should focus on operations requiring real infrastructure, especially:

- Database persistence
- Repository operations
- CV creation and retrieval
- CV deletion
- Database constraints and entity relationships

Use Testcontainers rather than requiring an independently configured development database.

Integration tests must not modify production data.

### Test Execution

Run all tests:

```bash
mvn clean test
```

Run a specific test class:

```bash
mvn -Dtest=JwtServiceTest test
```

Run a production build:

```bash
mvn clean package
```

Tests using Testcontainers require a running Docker environment.

---

## ✅ Before Submitting a Pull Request

Before submitting, verify:

- [ ] The application builds successfully
- [ ] Relevant unit tests pass
- [ ] Relevant integration tests pass
- [ ] Existing functionality remains operational
- [ ] API responses and HTTP status codes are correct
- [ ] No credentials or secrets were committed
- [ ] No unnecessary personal information is logged
- [ ] Uploaded documents are validated appropriately
- [ ] AI inputs and outputs are handled securely
- [ ] Database migrations are included when required
- [ ] Swagger documentation was updated when necessary
- [ ] README was updated if setup or behavior changed
- [ ] Privacy Policy was updated if data processing changed
- [ ] Release notes were updated for notable changes
- [ ] No unrelated refactoring or unnecessary dependencies were introduced

For AI-related changes, additionally verify:

- [ ] Trusted instructions remain separate from untrusted input
- [ ] JSON schemas remain compatible with response DTOs
- [ ] Missing or invalid AI output is handled safely
- [ ] Candidate analysis excludes unnecessary personal characteristics
- [ ] Automated tests do not require real OpenAI API credentials

---

## 🐛 Reporting Bugs

Bug reports should include:

- Application version
- Java version
- Operating system
- Steps to reproduce the issue
- Expected behavior
- Actual behavior
- Relevant HTTP status codes
- Relevant application logs
- A minimal reproducible example, when possible

For database-related problems, also provide:

- PostgreSQL version
- Relevant migration information
- Database error messages without sensitive values

For AI-related problems, provide:

- The affected API operation
- The type of error or response received
- Relevant sanitized diagnostic information

Do not include:

- OpenAI API keys
- JWT tokens
- Passwords or database credentials
- Authorization headers
- Real candidate CVs
- Personal contact details
- Private database contents
- Sensitive application configuration

> [!NOTE]
> Use synthetic or anonymized CV data when reporting bugs or demonstrating unexpected behavior.

---

## 📜 License

This project is licensed under the **Apache License 2.0**. See [LICENSE](LICENSE) for details.

Unless explicitly stated otherwise, contributions submitted to this project are provided under the same license.

By submitting a contribution, you confirm that you have the right to provide it under the Apache License 2.0.

---

Thank you for contributing to CV Insight!
