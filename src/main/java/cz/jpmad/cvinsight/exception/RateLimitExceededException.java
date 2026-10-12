package cz.jpmad.cvinsight.exception;

/**
 * Thrown when the OpenAI API rate limit is exceeded.
 */
public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
