package cz.jpmad.cvinsight.service.cv;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CvExtractionServiceTest {

    private CvExtractionService extractionService;

    @BeforeEach
    void setUp() {
        extractionService = new CvExtractionService(List.of(
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        ));
    }

    @Test
    void extractTextShouldRejectNullFile() {
        assertThrows(
                IllegalArgumentException.class,
                () -> extractionService.extractText(null)
        );
    }

    @Test
    void extractTextShouldRejectEmptyFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.pdf",
                "application/pdf",
                new byte[0]
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> extractionService.extractText(file)
        );
    }

    @Test
    void extractTextShouldRejectUnsupportedFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "notes.txt",
                "text/plain",
                "Hello world".getBytes()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> extractionService.extractText(file)
        );
    }

    @Test
    void extractTextShouldExtractPdfContent() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "cv.pdf",
                "application/pdf",
                createPdf("Java Developer")
        );

        String result = extractionService.extractText(file);

        assertTrue(result.contains("Java Developer"));
    }

    private byte[] createPdf(final String text) throws Exception {
        try (
                PDDocument document = new PDDocument();
                ByteArrayOutputStream output = new ByteArrayOutputStream()
        ) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream content =
                         new PDPageContentStream(document, page)) {

                content.beginText();
                content.setFont(
                        new PDType1Font(Standard14Fonts.FontName.HELVETICA),
                        12
                );
                content.newLineAtOffset(50, 700);
                content.showText(text);
                content.endText();
            }

            document.save(output);

            return output.toByteArray();
        }
    }
}