# Privacy Policy

**CV Insight REST API**  
**Version:** 1.0.0  
**Last Updated:** October 11, 2026

---

## 1. Introduction

CV Insight is a REST API for processing and analyzing curriculum vitae (CV) documents.

This Privacy Policy describes the types of information the application may process, how that information is used, and the technical measures implemented to protect it.

CV Insight is developed by **Petr Šafrata**.

The application is designed primarily for educational, development, and demonstration purposes.

## 2. Categories of Processed Data

Depending on the API operation, CV Insight may process the following categories of information.

### 2.1 Personal and Contact Information

- First and last name.
- Email address.
- Telephone number.
- Postal address.
- City and region.
- Postal code.
- Nationality and gender, if provided in the source document.

### 2.2 Professional Information

- Employment history.
- Job positions and employers.
- Education and qualifications.
- Language proficiency.
- Driving licenses.
- Other information included in the submitted CV text.

### 2.3 Authentication Information

The application processes authentication-related information, including:

- Usernames.
- Password verification data.
- JWT authentication tokens.

### 2.4 Technical Information

The application may generate technical logs containing information such as:

- API operation details.
- Processing results.
- Application errors.
- External API response statuses.
- Technical identifiers necessary for debugging and maintenance.

The application is designed to avoid logging complete CV content or external API credentials.

## 3. Purposes of Data Processing

Information is processed to support the following operations:

1. Extracting readable text from CV documents.
2. Detecting hidden or suspicious document content.
3. Converting unstructured CV text into structured candidate records.
4. Persisting structured CV information.
5. Retrieving and deleting stored CV records.
6. Comparing professional qualifications against job requirements.
7. Authenticating users and protecting API endpoints.
8. Monitoring application operation and diagnosing technical errors.

## 4. Document Processing

### 4.1 Text Extraction

The application uses document-processing libraries to extract text from supported document formats.

Text extraction does not require artificial intelligence.

### 4.2 Suspicious Content Detection

The application inspects supported document formats for potentially suspicious content, including:

- Hidden text.
- Unusually small font sizes.
- White or difficult-to-read text.
- Text located outside visible page boundaries.

Detection capabilities depend on the document format.

This operation does not require artificial intelligence.

### 4.3 Structured CV Parsing

Extracted CV text may be submitted to the OpenAI API to generate structured candidate information.

The resulting structured data may contain personal and professional information from the original document.

### 4.4 Candidate Analysis

When comparing a stored CV with a job description, the application constructs an analysis input containing relevant professional information.

The candidate information submitted for analysis is designed to include:

- Language skills.
- Driving licenses.
- Employment experience.
- Education.

Direct personal identifiers and personal characteristics, such as name, email address, telephone number, gender, and nationality, are excluded from the candidate profile constructed for this operation.

The supplied job description is also processed during AI analysis.

## 5. External AI Processing

CV Insight integrates with the **OpenAI Responses API** for CV parsing and candidate analysis.

Relevant input data is transmitted to OpenAI when these AI-powered endpoints are used.

The handling of data by OpenAI is subject to the applicable service terms, data processing conditions, and privacy documentation.

For additional information, refer to:

- https://openai.com/policies/privacy-policy/
- https://openai.com/policies/

The application does not independently control the retention or processing practices of external API providers.

## 6. Data Storage

Structured CV records are stored in a **PostgreSQL** database.

Stored information may contain personal and professional details extracted from candidate CVs.

The API provides functionality to:

- Create a CV record.
- Retrieve stored CV records.
- List stored CV records.
- Delete a selected CV record.

Uploaded documents and extracted text are processed as part of API requests. Persistent storage of the original uploaded document is not part of the described CV management functionality.

Database backup, infrastructure storage, and hosting arrangements depend on the deployment environment.

## 7. Data Retention and Deletion

CV Insight provides an API operation for deleting individual stored CV records.

Retention periods depend on the deployment configuration and the policies of the organization operating the application.

The application does not establish a universal automatic deletion period for stored CV records.

Deletion of a database record does not necessarily remove data from existing backups, external service records, or technical logs.

The organization operating the application is responsible for establishing an appropriate retention and deletion policy.

## 8. Security Measures

CV Insight implements technical security measures including:

- JWT-based authentication.
- Protected REST API endpoints.
- Request validation.
- Separation of trusted AI instructions from untrusted CV content.
- Structured JSON output definitions for AI processing.
- Error handling for failed external API requests.
- Database access through the application's persistence layer.
- Avoidance of sensitive CV content in normal application logs.

No software system can guarantee absolute protection against all security threats.

Deployments should additionally use HTTPS, appropriate network restrictions, secure credential management, and database access controls.

## 9. Prompt Injection and Untrusted Content

CV documents and job descriptions are treated as untrusted content.

Instructions embedded in uploaded documents are not intended to override the application's trusted instructions or security requirements.

The application implements measures intended to reduce the risk of prompt injection and unauthorized manipulation of AI processing.

These measures do not constitute a guarantee against every possible malicious input.

## 10. Data Controller and Legal Responsibility

CV Insight is software that may be operated by different individuals or organizations.

The person or organization determining why and how personal information is processed is responsible for identifying the applicable legal basis, meeting transparency obligations, and determining its role under relevant data protection laws.

Where applicable, this includes obligations under the **General Data Protection Regulation (GDPR)**.

This document does not establish a legal basis for collecting or processing candidate data.

## 11. Data Subject Rights

Depending on applicable law and the deployment context, individuals may have rights concerning their personal information, including:

- Access to personal data.
- Correction of inaccurate information.
- Erasure of personal data.
- Restriction of processing.
- Objection to certain processing activities.
- Data portability, where applicable.

Requests should be directed to the organization operating the relevant deployment of CV Insight.

## 12. Children's Privacy

CV Insight is not specifically designed to collect or process information from children.

Operators must ensure that any processing of minors' personal data complies with applicable legal requirements.

## 13. Changes to This Privacy Policy

This Privacy Policy may be updated when application functionality, infrastructure, or data processing practices change.

The current version will be made available through the project's documentation.

## 14. Contact

For questions concerning the CV Insight software project:

**Developer:** Petr Šafrata  
**Website:** https://petrsafrata.cz/

For privacy requests involving an independently hosted deployment, contact the organization responsible for that deployment.

---

*This document describes the application's intended technical data processing. Operators must adapt it to their actual deployment, legal responsibilities, and data handling practices.*
