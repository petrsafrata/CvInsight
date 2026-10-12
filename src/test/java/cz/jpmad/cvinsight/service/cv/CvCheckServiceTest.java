package cz.jpmad.cvinsight.service.cv;

import cz.jpmad.cvinsight.exception.UnsupportedCvFileTypeException;
import cz.jpmad.cvinsight.service.cv.checker.DocCvChecker;
import cz.jpmad.cvinsight.service.cv.checker.DocxCvChecker;
import cz.jpmad.cvinsight.service.cv.checker.PdfCvChecker;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CvCheckServiceTest {

    private PdfCvChecker pdfChecker;
    private DocCvChecker docChecker;
    private DocxCvChecker docxChecker;
    private CvCheckService checkService;

    @BeforeEach
    void setUp() {
        pdfChecker = mock(PdfCvChecker.class);
        docChecker = mock(DocCvChecker.class);
        docxChecker = mock(DocxCvChecker.class);

        checkService = new CvCheckService(
                pdfChecker,
                docChecker,
                docxChecker,
                List.of(
                        "application/pdf",
                        "application/msword",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                ),
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        );
    }

    @Test
    void checkShouldRejectNullFile() {
        assertThrows(
                IllegalArgumentException.class,
                () -> checkService.check(null)
        );
    }

    @Test
    void checkShouldRejectEmptyFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                new byte[0]
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> checkService.check(file)
        );
    }

    @Test
    void checkShouldRejectUnsupportedFormat() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.txt",
                "text/plain",
                "Some text".getBytes()
        );

        assertThrows(
                UnsupportedCvFileTypeException.class,
                () -> checkService.check(file)
        );

        verifyNoInteractions(pdfChecker, docChecker, docxChecker);
    }

    @Test
    void checkShouldUseDocxChecker() throws Exception {
        byte[] content;

        try (
                XWPFDocument document = new XWPFDocument();
                ByteArrayOutputStream output = new ByteArrayOutputStream()
        ) {
            document.createParagraph().createRun().setText("CV content");
            document.write(output);
            content = output.toByteArray();
        }

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "cv.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                content
        );

        when(docxChecker.check(any(InputStream.class)))
                .thenReturn(List.of());

        var result = checkService.check(file);

        assertFalse(result.suspicious());

        verify(docxChecker).check(any(InputStream.class));
        verifyNoInteractions(pdfChecker, docChecker);
    }
}