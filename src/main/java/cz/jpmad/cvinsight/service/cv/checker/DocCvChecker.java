package cz.jpmad.cvinsight.service.cv.checker;

import cz.jpmad.cvinsight.api.cv.dto.CvIssueResponse;
import cz.jpmad.cvinsight.model.cv.CvIssueType;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks legacy DOC documents for hidden or suspicious text.
 */
@Component
public class DocCvChecker {

    private final double minReadableFontSize;

    public DocCvChecker(@Value("${cv.check.min-readable-font-size}") final double minReadableFontSize) {
        this.minReadableFontSize = minReadableFontSize;
    }

    /**
     * Checks a DOC document for suspicious text formatting.
     *
     * @param inputStream DOC document input stream
     * @return detected suspicious content
     * @throws IOException if the document cannot be read
     */
    public List<CvIssueResponse> check(final InputStream inputStream) throws IOException {
        final List<CvIssueResponse> issues = new ArrayList<>();

        try (HWPFDocument document = new HWPFDocument(inputStream)) {
            final Range range = document.getRange();

            for (int i = 0; i < range.numCharacterRuns(); i++) {
                final CharacterRun run = range.getCharacterRun(i);
                checkRun(run, issues);
            }
        }

        return issues;
    }

    /**
     * Checks a character run for hidden or suspicious text formatting.
     *
     * @param run    character run to check
     * @param issues list to collect detected issues
     */
    private void checkRun(final CharacterRun run, final List<CvIssueResponse> issues) {
        final String text = run.text();

        if (text == null || text.isBlank()) {
            return;
        }

        final String normalizedText = text.trim();

        if (run.isVanished()) {
            issues.add(new CvIssueResponse(
                    CvIssueType.HIDDEN_TEXT,
                    normalizedText,
                    null
            ));
        }

        final double fontSize = run.getFontSize() / 2.0;

        if (fontSize > 0 && fontSize < minReadableFontSize) {
            issues.add(new CvIssueResponse(
                    CvIssueType.TINY_FONT,
                    normalizedText,
                    (float) fontSize
            ));
        }
    }
}