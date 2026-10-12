package cz.jpmad.cvinsight.exception;

/**
 * Exception thrown when the OpenAI API is unavailable.
 */
public class OpenAiUnavailableException extends RuntimeException {

    public OpenAiUnavailableException(
            final String message,
            final Throwable cause
    ) {
        super(message, cause);
    }
}