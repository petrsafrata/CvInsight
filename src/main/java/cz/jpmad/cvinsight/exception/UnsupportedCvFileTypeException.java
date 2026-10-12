package cz.jpmad.cvinsight.exception;

/**
 * Thrown when an unsupported CV document type is provided.
 */
public class UnsupportedCvFileTypeException extends RuntimeException {

    public UnsupportedCvFileTypeException(final String mediaType) {
        super("Unsupported CV file type: " + mediaType);
    }
}