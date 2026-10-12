package cz.jpmad.cvinsight.service.cv;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Provides text extraction from CV documents.
 * Supports configured document formats and uses Apache Tika
 * for media type detection and text extraction.
 */
@Slf4j
@Service
public class CvExtractionService {

    private final List<String> supportedMediaTypes;

    private final Tika tika = new Tika();

    public CvExtractionService(@Value("${cv.extraction.supported-media-types}") final List<String> supportedMediaTypes) {
        this.supportedMediaTypes = supportedMediaTypes;
    }

    /**
     * Extracts plain text from a CV document.
     *
     * @param file CV document
     * @return extracted plain text
     * @throws IOException if the document cannot be read
     * @throws TikaException if Apache Tika cannot parse the document
     */
    public String extractText(final MultipartFile file) throws IOException, TikaException {
        validateFile(file);

        final String fileName = file.getOriginalFilename();
        final String mediaType = detectMediaType(file);

        log.info("Extracting text from CV: fileName={}, size={}, mediaType={}",
                fileName,
                file.getSize(),
                mediaType
        );

        validateMediaType(mediaType);

        try (InputStream inputStream = file.getInputStream()) {
            final String text = tika.parseToString(inputStream).trim();

            log.info("CV text extraction completed: fileName={}, characters={}",
                    fileName,
                    text.length()
            );

            return text;
        } catch (IOException | TikaException e) {
            log.error("Failed to extract text from CV: fileName={}", fileName, e);
            throw e;
        }
    }

    /**
     * Detects the media type of a CV document using Apache Tika.
     *
     * @param file CV document
     * @return detected media type
     * @throws IOException if the document cannot be read
     */
    private String detectMediaType(final MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            final String mediaType = tika.detect(
                    inputStream,
                    file.getOriginalFilename()
            );

            log.debug("Detected CV media type: fileName={}, mediaType={}", file.getOriginalFilename(), mediaType);

            return mediaType;
        }
    }

    /**
     * Validates that the provided file is not null or empty.
     *
     * @param file CV document
     * @throws IllegalArgumentException if the file is null or empty
     */
    private void validateFile(final MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("CV text extraction requested with an empty file");
            throw new IllegalArgumentException("CV file must not be empty");
        }
    }

    /**
     * Validates that the provided media type is supported.
     *
     * @param mediaType detected media type
     * @throws IllegalArgumentException if the media type is not supported
     */
    private void validateMediaType(final String mediaType) {
        if (!supportedMediaTypes.contains(mediaType)) {
            log.warn("Unsupported CV media type: {}", mediaType);
            throw new IllegalArgumentException("Unsupported CV file type: " + mediaType);
        }
    }
}