package cz.jpmad.cvinsight.config;

import cz.jpmad.cvinsight.api.ApiEndpoints;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures OpenAPI documentation for the CV Insight REST API.
 */
@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final ApiEndpoints apiEndpoints;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CV Insight REST API")
                        .version(apiEndpoints.getApiVersion())
                        .description("""
                                # CV Insight REST API

                                REST API for extracting, inspecting, parsing, storing,
                                and analyzing candidate CVs.

                                Built with **Spring Boot**, CV Insight combines document
                                processing, AI-powered analysis, and relational data storage.

                                ---

                                ## Features

                                - **JWT Authentication** - secure access to protected API endpoints
                                - **Text Extraction** - extract text from PDF, DOC, and DOCX without AI
                                - **Document Inspection** - detect hidden or suspicious text and formatting
                                - **AI CV Parsing** - convert unstructured CV text into structured JSON
                                - **CV Management** - store, retrieve, list, and delete CV records
                                - **AI Candidate Analysis** - compare stored CVs with job requirements
                                - **PostgreSQL Storage** - persist structured candidate information
                                - **Swagger UI** - interactive REST API documentation

                                ---

                                ## CV Processing Workflow

                                CV Insight separates document processing into five independent stages.

                                ### 1. Extract

                                Upload a PDF, DOC, or DOCX document and extract its plain text
                                using Apache Tika, Apache PDFBox, or Apache POI.

                                This operation does **not** use artificial intelligence.

                                **Endpoint:** `POST /api/cv/extract`

                                ### 2. Check

                                Inspect the original document and its formatting to identify
                                potentially suspicious content, including:

                                - hidden text
                                - extremely small font sizes
                                - white or difficult-to-read text
                                - text positioned outside the visible page area

                                Detection capabilities depend on the document format.

                                This operation does **not** use artificial intelligence.

                                **Endpoint:** `POST /api/cv/check`

                                ### 3. Parse

                                Convert extracted CV text into structured candidate information
                                using the OpenAI Responses API.

                                The resulting data may include:

                                - personal and contact information
                                - address and location
                                - language proficiency (A1-C2)
                                - driving licenses
                                - recent work experience
                                - education

                                Missing or unverifiable information should be represented
                                as `null` or an empty collection rather than invented.

                                **Endpoint:** `POST /api/cv/parse`

                                ### 4. Store

                                Persist structured CV data in a PostgreSQL database.

                                Supported operations include creating, listing, retrieving,
                                and deleting stored CV records.

                                **Endpoints:**

                                - `POST /api/cvs`
                                - `GET /api/cvs`
                                - `GET /api/cvs/{id}`
                                - `DELETE /api/cvs/{id}`

                                ### 5. Analyze

                                Compare a stored CV with a job description using OpenAI.

                                The analysis identifies:

                                - relevant professional experience
                                - matching skills and qualifications
                                - missing or unconfirmed requirements

                                Candidate evaluation should be based on professional
                                qualifications rather than personal characteristics.

                                **Endpoint:** `POST /api/cvs/{id}/analysis`

                                ---

                                ## AI Security and Data Handling

                                CV documents and job descriptions are treated as
                                **untrusted input**.

                                Instructions embedded in candidate documents must not
                                override application instructions or influence AI behavior.

                                AI processing is designed to:

                                - treat document content as data rather than instructions
                                - ignore prompt injection attempts
                                - avoid fabricating missing candidate information
                                - evaluate job-related qualifications
                                - avoid using protected personal characteristics in candidate analysis

                                Structured JSON schemas are used to define expected AI outputs.

                                ---

                                ## Authentication and Authorization

                                CV Insight uses **JSON Web Tokens (JWT)** for authentication.

                                1. Authenticate with a username and password.
                                2. Receive a JWT access token.
                                3. Include the token in requests to protected endpoints.

                                **Authorization header:**

                                `Authorization: Bearer <token>`

                                JWT access tokens have a limited validity period.

                                All API endpoints except the login endpoint require authentication.

                                ### Swagger UI Authentication

                                1. Call the login endpoint to obtain a JWT.
                                2. Click **Authorize** in Swagger UI.
                                3. Enter the JWT access token.
                                4. Execute protected API requests.

                                **Login endpoint:** `POST /api/auth/login`

                                ---

                                ## API Endpoint Groups

                                | Group | Description |
                                |-------|-------------|
                                | `/api/auth` | User authentication and JWT generation |
                                | `/api/cv` | Document extraction, inspection, and AI parsing |
                                | `/api/cvs` | Stored CV management and AI candidate analysis |

                                ---

                                ## Common HTTP Responses

                                | Status | Description |
                                |--------|-------------|
                                | `200 OK` | Request completed successfully |
                                | `201 Created` | Resource created successfully |
                                | `204 No Content` | Request completed without a response body |
                                | `400 Bad Request` | Invalid request parameters or validation errors |
                                | `401 Unauthorized` | Missing, expired, or invalid authentication |
                                | `403 Forbidden` | Access denied |
                                | `404 Not Found` | Requested resource does not exist |
                                | `409 Conflict` | Resource or database constraint conflict |
                                | `415 Unsupported Media Type` | Unsupported CV file format |
                                | `429 Too Many Requests` | Request or external API rate limit exceeded |
                                | `500 Internal Server Error` | Unexpected application error |
                                | `502 Bad Gateway` | Invalid or rejected response from the OpenAI API |
                                | `503 Service Unavailable` | OpenAI API or another required service is temporarily unavailable |

                                ### Error Response Format

                                API errors are returned as structured JSON responses.

                                Example:

                                ```json
                                {
                                  "timestamp": "2026-10-10T15:00:00+02:00",
                                  "code": "BAD_REQUEST",
                                  "message": "Invalid request parameters"
                                }
                                ```

                                Validation errors may also include a `details` field
                                describing individual field violations.

                                ---

                                ## Technology Stack

                                - Java
                                - Spring Boot
                                - Spring Security and JWT
                                - Spring Data JPA
                                - PostgreSQL
                                - Flyway
                                - Apache Tika, Apache PDFBox, and Apache POI
                                - OpenAI Responses API
                                - Swagger / OpenAPI
                                - Docker
                                """)
                        .contact(new Contact()
                                .name("Petr Šafrata")
                                .url("https://petrsafrata.cz/"))
                        .license(new License()
                                .name("Apache-2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(
                        new SecurityRequirement().addList("bearerAuth")
                )
                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}