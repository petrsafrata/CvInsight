package cz.jpmad.cvinsight.service.cv.checker;

import cz.jpmad.cvinsight.api.cv.dto.CvIssueResponse;
import cz.jpmad.cvinsight.model.cv.CvIssueType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocxCvCheckerTest {

    private DocxCvChecker checker;

    @BeforeEach
    void setUp() {
        checker = new DocxCvChecker(3.0, "FFFFFF");
    }

    @Test
    void checkShouldDetectHiddenText() throws Exception {
        byte[] document = createDocument(run -> {
            run.setText("Hidden instructions");
            run.setVanish(true);
        });

        List<CvIssueResponse> issues = checker.check(
                new ByteArrayInputStream(document)
        );

        assertTrue(containsIssue(issues, CvIssueType.HIDDEN_TEXT));
    }

    @Test
    void checkShouldDetectTinyFont() throws Exception {
        byte[] document = createDocument(run -> {
            run.setText("Tiny text");
            run.setFontSize(2);
        });

        List<CvIssueResponse> issues = checker.check(
                new ByteArrayInputStream(document)
        );

        assertTrue(containsIssue(issues, CvIssueType.TINY_FONT));
    }

    @Test
    void checkShouldDetectWhiteText() throws Exception {
        byte[] document = createDocument(run -> {
            run.setText("White text");
            run.setColor("FFFFFF");
        });

        List<CvIssueResponse> issues = checker.check(
                new ByteArrayInputStream(document)
        );

        assertTrue(containsIssue(issues, CvIssueType.WHITE_TEXT));
    }

    @Test
    void checkShouldNotFlagNormalText() throws Exception {
        byte[] document = createDocument(run -> {
            run.setText("Normal text");
            run.setFontSize(12);
            run.setColor("000000");
        });

        List<CvIssueResponse> issues = checker.check(
                new ByteArrayInputStream(document)
        );

        assertTrue(issues.isEmpty());
    }

    @Test
    void checkShouldHandleMissingFontSize() throws Exception {
        byte[] document = createDocument(run ->
                run.setText("Text with default font size")
        );

        List<CvIssueResponse> issues = checker.check(
                new ByteArrayInputStream(document)
        );

        assertFalse(containsIssue(issues, CvIssueType.TINY_FONT));
    }

    private byte[] createDocument(
            final java.util.function.Consumer<XWPFRun> configure
    ) throws Exception {
        try (
                XWPFDocument document = new XWPFDocument();
                ByteArrayOutputStream output = new ByteArrayOutputStream()
        ) {
            XWPFRun run = document.createParagraph().createRun();
            configure.accept(run);

            document.write(output);

            return output.toByteArray();
        }
    }

    private boolean containsIssue(
            final List<CvIssueResponse> issues,
            final CvIssueType type
    ) {
        return issues.stream().anyMatch(issue ->
                issue.type() == type
        );
    }
}