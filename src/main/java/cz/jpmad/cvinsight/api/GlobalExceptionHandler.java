package cz.jpmad.cvinsight.api;

import cz.jpmad.cvinsight.exception.OpenAiUnavailableException;
import cz.jpmad.cvinsight.exception.OpenApiException;
import cz.jpmad.cvinsight.exception.RateLimitExceededException;
import cz.jpmad.cvinsight.exception.UnsupportedCvFileTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Global exception handling for REST APIs.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handling invalid arguments (400).
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex) {
        log.warn("Unvalid request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error("BAD_REQUEST", ex.getMessage()));
    }

    /**
     * Conflict handling (409).
     */
    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<Map<String, Object>> handleConflict(IllegalStateException ex) {
        log.warn("Conflict: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error("CONFLICT", ex.getMessage()));
    }

    /**
     * Handling validation errors (400).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of(
                        "field", fe.getField(),
                        "message", fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value"
                ))
                .toList();
        log.warn("Validation failed: {}", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error("VALIDATION_ERROR",
                "Validation failed for one or more fields", fieldErrors.toString()
        ));
    }

    /**
     * Handling data binding errors (400).
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, Object>> handleBind(BindException ex) {
        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of(
                        "field", fe.getField(),
                        "message", fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value"
                ))
                .toList();
        log.warn("Binding failed: {}", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error("VALIDATION_ERROR",
                "Binding failed for one or more fields", fieldErrors.toString()));
    }

    /**
     * Processing found entities (404).
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException ex) {
        log.warn("Entity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error("NOT_FOUND", ex.getMessage()));
    }

    /**
     * Handling users not found (404).
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        log.warn("Username not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error("NOT_FOUND", ex.getMessage()));
    }

    /**
     * Processing authorization denied (403).
     */
    @ExceptionHandler({AuthorizationDeniedException.class})
    public ResponseEntity<Map<String, Object>> handleForbidden(AuthorizationDeniedException ex) {
        log.warn("Authorization denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error("FORBIDDEN", "Authorization denied"));
    }

    /**
     * Data integrity violation processing (409).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error("CONFLICT", "Unique constraint violated"));
    }

    /**
     * Handling bad credentials (401).
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Bad credentials: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(error("UNAUTHORIZED", "Bad credentials"));
    }

    /**
     * Handling access denied (403).
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error("FORBIDDEN", "Access denied"));
    }

    /**
     * Unsupported Media Type (415).
     */
    @ExceptionHandler(UnsupportedCvFileTypeException.class)
    public ResponseEntity<Map<String, Object>> handleDenied(UnsupportedCvFileTypeException ex) {
        log.warn("Unsupported media type: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error("UNSUPPORTED_MEDIA_TYPE", ex.getMessage()));
    }

    /**
     * Too many requests (429).
     */
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleDenied(RateLimitExceededException ex) {
        log.warn("Too many requests: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error("TOO_MANY_REQUESTS", "Too many requests"));
    }

    /**
     * OpenApi error (502).
     */
    @ExceptionHandler(OpenApiException.class)
    public ResponseEntity<Map<String, Object>> handleDenied(OpenApiException ex) {
        log.warn("Bad gateway: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error("BAD_GATEWAY", ex.getMessage()));
    }

    /**
     * OpenAI API unavailable (503).
     */
    @ExceptionHandler(OpenAiUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handleOpenAiUnavailable(OpenAiUnavailableException ex) {
        log.warn("OpenAI API unavailable: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(error("SERVICE_UNAVAILABLE", "OpenAI API is temporarily unavailable"));
    }

    /**
     * Handling exceptions with set HTTP status.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException ex) {
        log.warn("Response status exception: {}", ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode())
                .body(error(ex.getStatusCode().toString(), ex.getReason() != null ? ex.getReason() : ex.getMessage()));
    }

    /**
     * Handling I/O errors when loading resources (500).
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleIO(IOException ex) {
        log.error("I/O error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error("INTERNAL_SERVER_ERROR", "Failed to read application resource"));
    }

    /**
     * Handling unexpected exceptions (500).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOther(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error("INTERNAL_SERVER_ERROR", "Unexpected error"));
    }

    /**
     * Helper method for creating a structured error response.
     */
    private Map<String, Object> error(String code, String message) {
        return Map.of(
                "timestamp", OffsetDateTime.now().toString(),
                "code", code,
                "message", message
        );
    }

    /**
     * Overloaded helper method for creating a structured error response with details.
     */
    private Map<String, Object> error(String code, String message, String details) {
        return Map.of(
                "timestamp", OffsetDateTime.now().toString(),
                "code", code,
                "message", message,
                "details", details
        );
    }
}
