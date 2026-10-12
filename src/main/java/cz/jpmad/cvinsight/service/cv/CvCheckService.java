package cz.jpmad.cvinsight.service.cv;

import cz.jpmad.cvinsight.api.cv.dto.CvCheckResponse;
import cz.jpmad.cvinsight.api.cv.dto.CvIssueResponse;
import cz.jpmad.cvinsight.exception.UnsupportedCvFileTypeException;
import cz.jpmad.cvinsight.service.cv.checker.DocCvChecker;
import cz.jpmad.cvinsight.service.cv.checker.DocxCvChecker;
import cz.jpmad.cvinsight.service.cv.checker.PdfCvChecker;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Provides suspicious content detection for CV documents.
 */
@Slf4j
@Service
public class CvCheckService {

    private final PdfCvChecker pdfCvChecker;
    private final DocCvChecker docCvChecker;
    private final DocxCvChecker docxCvChecker;

    private final List<String> supportedMediaTypes;

    private final String pdfMediaType;
    private final String docMediaType;
    private final String docxMediaType;

    private final Tika tika = new Tika();

    public CvCheckService(
            final PdfCvChecker pdfCvChecker,
            final DocCvChecker docCvChecker,
            final DocxCvChecker docxCvChecker,
            @Value("${cv.extraction.supported-media-types}") final List<String> supportedMediaTypes,
            @Value("${cv.check.media-types.pdf}") final String pdfMediaType,
            @Value("${cv.check.media-types.doc}") final String docMediaType,
            @Value("${cv.check.media-types.docx}") final String docxMediaType
    ) {
        this.pdfCvChecker = pdfCvChecker;
        this.docCvChecker = docCvChecker;
        this.docxCvChecker = docxCvChecker;
        this.supportedMediaTypes = supportedMediaTypes;
        this.pdfMediaType = pdfMediaType;
        this.docMediaType = docMediaType;
        this.docxMediaType = docxMediaType;
    }

    /**
     * Checks a CV document for hidden or suspicious text.
     *
     * @param file original CV document
     * @return suspicious content analysis
     * @throws IOException if the document cannot be read
     */
    public CvCheckResponse check(final MultipartFile file) throws IOException {
        validateFile(file);

        final String mediaType = detectMediaType(file);

        log.info("Checking CV for suspicious content: fileName={}, size={}, mediaType={}",
                file.getOriginalFilename(),
                file.getSize(),
                mediaType
        );

        validateMediaType(mediaType);

        final List<CvIssueResponse> issues;

        try (InputStream inputStream = file.getInputStream()) {
            if (pdfMediaType.equals(mediaType)) {
                issues = pdfCvChecker.check(inputStream);
            } else if (docMediaType.equals(mediaType)) {
                issues = docCvChecker.check(inputStream);
            } else if (docxMediaType.equals(mediaType)) {
                issues = docxCvChecker.check(inputStream);
            } else {
                throw new UnsupportedCvFileTypeException(mediaType);
            }
        }

        log.info("CV suspicious content check completed: fileName={}, issues={}",
                file.getOriginalFilename(),
                issues.size()
        );

        return new CvCheckResponse(!issues.isEmpty(), issues);
    }

    /**
     * Detects the media type of a CV document.
     *
     * @param file original CV document
     * @return detected media type
     * @throws IOException if the document cannot be read
     */
    private String detectMediaType(final MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            final String mediaType = tika.detect(
                    inputStream,
                    file.getOriginalFilename()
            );

            log.debug("Detected CV media type: fileName={}, mediaType={}",
                    file.getOriginalFilename(),
                    mediaType
            );

            return mediaType;
        }
    }

    /**
     * Validates that the CV document is not empty.
     *
     * @param file original CV document
     * @throws IllegalArgumentException if the document is empty
     */
    private void validateFile(final MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("CV check requested with an empty file");
            throw new IllegalArgumentException("CV file must not be empty");
        }
    }

    /**
     * Validates that the CV document's media type is supported.
     *
     * @param mediaType detected media type
     * @throws UnsupportedCvFileTypeException if the media type is not supported
     */
    private void validateMediaType(final String mediaType) {
        if (!supportedMediaTypes.contains(mediaType)) {
            log.warn("Unsupported CV media type: {}", mediaType);
            throw new UnsupportedCvFileTypeException(mediaType);
        }
    }
}