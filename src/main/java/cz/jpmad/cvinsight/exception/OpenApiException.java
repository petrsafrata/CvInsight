package cz.jpmad.cvinsight.exception;

/**
 * Thrown when the OpenAI API returns an error response.
 */
public class OpenApiException extends RuntimeException {
    public OpenApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
