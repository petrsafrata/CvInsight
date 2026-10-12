# Terms of Use

**CV Insight REST API**  
**Version:** 1.0.0  
**Last Updated:** October 11, 2026

---

## 1. Introduction

Welcome to CV Insight, a REST API designed to extract, inspect, parse, store, and analyze curriculum vitae (CV) documents.

CV Insight is developed by **Petr Šafrata** as a software development project.

By accessing or using the API, you agree to these Terms of Use. If you do not agree, you should not use the service.

## 2. Purpose of the Service

CV Insight provides the following functionality:

- Extraction of text from PDF, DOC, and DOCX documents.
- Detection of hidden or suspicious document content.
- Conversion of unstructured CV text into structured data using artificial intelligence.
- Storage, retrieval, and deletion of structured CV records.
- Analysis of candidate qualifications against job requirements.
- Authentication and authorization using JSON Web Tokens (JWT).

The service is intended for educational, demonstration, development, and authorized CV-processing purposes.

## 3. Authorized Use

Users must:

- Access the API only through authorized accounts.
- Process CV documents they are legally permitted to use.
- Obtain appropriate authorization before submitting third-party personal data.
- Provide accurate information when interacting with the service.
- Comply with applicable privacy, employment, and data protection laws.

Users must not:

- Attempt to bypass authentication or security controls.
- Access, modify, or delete data without authorization.
- Upload malicious files or intentionally harmful content.
- Attempt to manipulate AI processing through embedded instructions or prompt injection.
- Use the service for unlawful discrimination or unauthorized profiling.
- Disrupt the availability, integrity, or security of the application.

## 4. Authentication and API Security

Access to protected API endpoints requires valid JWT authentication.

Users are responsible for protecting their credentials and access tokens.

Authentication tokens have a limited validity period and must not be shared with unauthorized individuals.

The service may reject requests containing invalid, expired, or unauthorized credentials.

## 5. CV Processing and Artificial Intelligence

CV Insight uses deterministic document-processing libraries for text extraction and suspicious-content detection.

Artificial intelligence is used for:

- Converting extracted CV text into structured candidate information.
- Analyzing professional qualifications against job descriptions.

AI-generated results may contain inaccuracies, omissions, or misinterpretations.

Users are responsible for reviewing and verifying generated information before relying on it.

**AI-generated candidate analysis must not be treated as the sole basis for employment decisions.**

Final decisions regarding recruitment, employment, and candidate suitability must remain under appropriate human oversight.

## 6. Fair and Responsible Candidate Evaluation

Candidate analysis is designed to focus on job-related qualifications, including:

- Relevant professional experience.
- Education and training.
- Language proficiency.
- Professional competencies and qualifications.
- Requirements specified in a job description.

The application is designed to exclude personal and contact information from the candidate profile supplied to AI analysis.

Users must not use the service to discriminate based on protected personal characteristics or other factors unrelated to legitimate job requirements.

## 7. Data Processing

The service may process personal information contained in uploaded CV documents.

Such information may include names, contact details, employment history, education, language proficiency, and other candidate-provided information.

Some processing operations use an external AI service provided by OpenAI.

Further details are described in the [Privacy Policy](PRIVACY_POLICY.md).

## 8. Service Availability

CV Insight is provided on an "as is" and "as available" basis.

The service may become temporarily unavailable due to:

- Scheduled maintenance.
- Software updates.
- Database connectivity issues.
- External API failures.
- Network interruptions.
- Resource or rate limits.

Continuous availability is not guaranteed.

## 9. Intellectual Property

The CV Insight application is distributed under the **Apache License 2.0**.

The license governs the use, modification, and distribution of the application source code.

Third-party libraries and services remain subject to their respective licenses and terms.

Users retain any applicable rights to the CV content they are authorized to process.

## 10. Limitation of Liability

To the extent permitted by applicable law, the developer is not responsible for damages resulting from:

- Incorrect or incomplete AI-generated outputs.
- Unauthorized use of the API.
- Improper handling of candidate information by users.
- Temporary service interruptions.
- Decisions made solely on the basis of automated analysis.

Nothing in these Terms excludes liability that cannot legally be excluded.

## 11. Changes to the Terms

These Terms may be updated to reflect changes in application functionality, security requirements, or applicable legal obligations.

Updates will be published through the project's documentation.

## 12. Contact

For questions regarding CV Insight or these Terms of Use:

**Developer:** Petr Šafrata  
**Website:** https://petrsafrata.cz/

---

*These Terms describe the intended use of the CV Insight software project and do not replace applicable legal obligations.*
