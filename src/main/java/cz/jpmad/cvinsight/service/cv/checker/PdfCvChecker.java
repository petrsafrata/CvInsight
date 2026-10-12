package cz.jpmad.cvinsight.service.cv.checker;

import cz.jpmad.cvinsight.api.cv.dto.CvIssueResponse;
import cz.jpmad.cvinsight.model.cv.CvIssueType;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks PDF documents for hidden or suspicious text.
 */
@Component
public class PdfCvChecker {

    private final float minReadableFontSize;
    private final float whiteColorThreshold;

    public PdfCvChecker(
            @Value("${cv.check.min-readable-font-size}") final float minReadableFontSize,
            @Value("${cv.check.white-color-threshold}") final float whiteColorThreshold
    ) {
        this.minReadableFontSize = minReadableFontSize;
        this.whiteColorThreshold = whiteColorThreshold;
    }

    /**
     * Checks a PDF document for suspicious text formatting.
     *
     * @param inputStream PDF document input stream
     * @return detected suspicious content
     * @throws IOException if the document cannot be read
     */
    public List<CvIssueResponse> check(final InputStream inputStream) throws IOException {
        final byte[] documentBytes = inputStream.readAllBytes();

        try (PDDocument document = Loader.loadPDF(documentBytes)) {
            final SuspiciousTextStripper stripper = new SuspiciousTextStripper(
                    minReadableFontSize,
                    whiteColorThreshold
            );

            stripper.getText(document);

            return stripper.getIssues();
        }
    }

    /**
     * PDF text stripper that inspects the original rendering properties
     * of individual text positions.
     */
    private static class SuspiciousTextStripper extends PDFTextStripper {

        private final float minReadableFontSize;
        private final float whiteColorThreshold;

        private final List<CvIssueResponse> issues = new ArrayList<>();

        private SuspiciousTextStripper(final float minReadableFontSize, final float whiteColorThreshold) {
            this.minReadableFontSize = minReadableFontSize;
            this.whiteColorThreshold = whiteColorThreshold;
        }

        private List<CvIssueResponse> getIssues() {
            return issues;
        }

        /**
         * Processes a single text position, checking for suspicious formatting such as tiny font size,
         * hidden text, white text, or text outside the page boundaries.
         *
         * @param textPosition The text to process.
         */
        @Override
        protected void processTextPosition(final TextPosition textPosition) {
            final String text = textPosition.getUnicode();

            if (text == null || text.isBlank()) {
                super.processTextPosition(textPosition);
                return;
            }

            checkFontSize(textPosition, text);
            checkRenderingMode(text);
            checkTextColor(text);
            checkPosition(textPosition, text);

            super.processTextPosition(textPosition);
        }

        /**
         * Checks if the font size of the text position is below the minimum readable font size.
         *
         * @param textPosition which to check the font size
         * @param text with the font size to check
         */
        private void checkFontSize(final TextPosition textPosition, final String text) {
            final float fontSize = textPosition.getFontSizeInPt();

            if (fontSize > 0 && fontSize < minReadableFontSize) {
                issues.add(new CvIssueResponse(
                        CvIssueType.TINY_FONT,
                        text,
                        fontSize
                ));
            }
        }

        /**
         * Checks the rendering mode of the text to determine if it is hidden or not rendered.
         *
         * @param text with the rendering mode to check
         */
        private void checkRenderingMode(final String text) {
            final RenderingMode renderingMode = getGraphicsState().getTextState().getRenderingMode();

            if (renderingMode == RenderingMode.NEITHER
                    || renderingMode == RenderingMode.NEITHER_CLIP) {

                issues.add(new CvIssueResponse(
                        CvIssueType.HIDDEN_TEXT,
                        text,
                        null
                ));
            }
        }

        /**
         * Checks if the text color is white, which may indicate hidden text on a white background.
         *
         * @param text with the color to check
         */
        private void checkTextColor(final String text) {
            final PDColor color = getGraphicsState().getNonStrokingColor();

            if (isWhite(color)) {
                issues.add(new CvIssueResponse(
                        CvIssueType.WHITE_TEXT,
                        text,
                        null
                ));
            }
        }

        /**
         * Checks if the text position is outside the page boundaries, which may indicate hidden or misplaced text.
         *
         * @param textPosition position to check if it is outside the page boundaries
         * @param text with the position to check if it is outside the page boundaries
         */
        private void checkPosition(final TextPosition textPosition, final String text) {
            final PDPage page = getCurrentPage();
            final PDRectangle pageBox = page.getCropBox();

            if (isOutsidePage(textPosition, pageBox)) {
                issues.add(new CvIssueResponse(
                        CvIssueType.OUTSIDE_PAGE,
                        text,
                        null
                ));
            }
        }

        /**
         * Checks if the given color is considered white based on the specified threshold.
         *
         * @param color for which to check if it is white
         * @return boolean indicating whether the color is white or not
         */
        private boolean isWhite(final PDColor color) {
            if (color == null) {
                return false;
            }

            try {
                final float[] rgb = color.getColorSpace()
                        .toRGB(color.getComponents());

                return rgb.length >= 3
                        && rgb[0] >= whiteColorThreshold
                        && rgb[1] >= whiteColorThreshold
                        && rgb[2] >= whiteColorThreshold;

            } catch (IOException | RuntimeException e) {
                return false;
            }
        }

        /**
         * Checks if the text position is outside the page boundaries defined by the given page box.
         *
         * @param textPosition for which to check if it is outside the page boundaries
         * @param pageBox of the page to check against
         * @return boolean indicating whether the text position is outside the page boundaries
         */
        private boolean isOutsidePage(final TextPosition textPosition, final PDRectangle pageBox) {
            final float x = textPosition.getXDirAdj();
            final float y = textPosition.getYDirAdj();

            return x < 0
                    || y < 0
                    || x > pageBox.getWidth()
                    || y > pageBox.getHeight();
        }
    }
}