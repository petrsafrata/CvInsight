package cz.jpmad.cvinsight.service.cv.checker;

import cz.jpmad.cvinsight.api.cv.dto.CvIssueResponse;
import cz.jpmad.cvinsight.model.cv.CvIssueType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks DOCX documents for hidden or suspicious text.
 */
@Component
public class DocxCvChecker {

    private final double minReadableFontSize;
    private final String whiteColor;

    public DocxCvChecker(
            @Value("${cv.check.min-readable-font-size}") final double minReadableFontSize,
            @Value("${cv.check.white-color}") final String whiteColor
    ) {
        this.minReadableFontSize = minReadableFontSize;
        this.whiteColor = normalizeColor(whiteColor);
    }

    /**
     * Checks a DOCX document for suspicious text formatting.
     *
     * @param inputStream DOCX document input stream
     * @return detected suspicious content
     * @throws IOException if the document cannot be read
     */
    public List<CvIssueResponse> check(final InputStream inputStream) throws IOException {
        final List<CvIssueResponse> issues = new ArrayList<>();

        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            document.getParagraphs().forEach(paragraph ->
                    checkParagraph(paragraph, issues)
            );

            document.getTables().forEach(table ->
                    table.getRows().forEach(row ->
                            row.getTableCells().forEach(cell ->
                                    cell.getParagraphs().forEach(paragraph ->
                                            checkParagraph(paragraph, issues)
                                    )
                            )
                    )
            );
        }

        return issues;
    }

    /**
     * Checks a paragraph for suspicious text formatting.
     *
     * @param paragraph the paragraph to check
     * @param issues the list of detected issues to add to
     */
    private void checkParagraph(final XWPFParagraph paragraph, final List<CvIssueResponse> issues) {
        paragraph.getRuns().forEach(run ->
                checkRun(run, issues)
        );
    }

    /**
     * Checks a run of text for suspicious formatting, such as hidden text, tiny font size, or white text color.
     *
     * @param run the run of text to check
     * @param issues for the list of detected issues to add to
     */
    private void checkRun(final XWPFRun run, final List<CvIssueResponse> issues) {
        final String text = run.text();

        if (text == null || text.isBlank()) {
            return;
        }

        checkHiddenText(run, text, issues);
        checkFontSize(run, text, issues);
        checkTextColor(run, text, issues);
    }

    /**
     * Checks if the run of text is hidden (vanished) and adds an issue if it is.
     *
     * @param run the run of text to check
     * @param text for the text content of the run
     * @param issues for the list of detected issues to add to
     */
    private void checkHiddenText(final XWPFRun run, final String text, final List<CvIssueResponse> issues) {
        if (run.isVanish()) {
            issues.add(new CvIssueResponse(
                    CvIssueType.HIDDEN_TEXT,
                    text,
                    null
            ));
        }
    }

    /**
     * Checks if the run of text has a font size smaller than the minimum readable font size and adds an issue if it does.
     *
     * @param run the run of text to check
     * @param text for the text content of the run
     * @param issues for the list of detected issues to add to
     */
    private void checkFontSize(final XWPFRun run, final String text, final List<CvIssueResponse> issues) {
        final Double fontSize = run.getFontSizeAsDouble();

        if (fontSize == null) {
            return;
        }

        if (fontSize > 0 && fontSize < minReadableFontSize) {
            issues.add(new CvIssueResponse(
                    CvIssueType.TINY_FONT,
                    text,
                    fontSize.floatValue()
            ));
        }
    }

    /**
     * Checks if the run of text has a color that matches the specified white color and adds an issue if it does.
     *
     * @param run the run of text to check
     * @param text for the text content of the run
     * @param issues for the list of detected issues to add to
     */
    private void checkTextColor(final XWPFRun run, final String text, final List<CvIssueResponse> issues) {
        final String color = run.getColor();

        if (color == null) {
            return;
        }

        if (whiteColor.equalsIgnoreCase(normalizeColor(color))) {
            issues.add(new CvIssueResponse(
                    CvIssueType.WHITE_TEXT,
                    text,
                    null
            ));
        }
    }

    /**
     * Normalizes a color string by trimming whitespace and removing the leading '#' character if present.
     * @param color for the color string to normalize
     * @return the normalized color string, or null if the input is null
     */
    private String normalizeColor(final String color) {
        if (color == null) {
            return null;
        }

        return color.trim().replaceFirst("^#", "");
    }
}