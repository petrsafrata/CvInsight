package cz.jpmad.cvinsight.api.cv;

import cz.jpmad.cvinsight.api.ApiEndpoints;
import cz.jpmad.cvinsight.api.Constants;
import cz.jpmad.cvinsight.api.cv.dto.CvCheckResponse;
import cz.jpmad.cvinsight.api.cv.dto.CvExtractResponse;
import cz.jpmad.cvinsight.api.cv.dto.CvParseRequest;
import cz.jpmad.cvinsight.api.cv.dto.CvParseResponse;
import cz.jpmad.cvinsight.service.cv.CvCheckService;
import cz.jpmad.cvinsight.service.cv.CvExtractionService;
import cz.jpmad.cvinsight.service.cv.CvParseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tika.exception.TikaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * REST controller for CV document processing.
 */
@RestController
@RequestMapping(ApiEndpoints.CV_ENDPOINT)
@RequiredArgsConstructor
@Tag(name = "CV", description = "CV document extraction, validation and processing")
public class CvController {

    private final CvExtractionService cvExtractionService;
    private final CvCheckService cvCheckService;
    private final CvParseService cvParseService;

    /**
     * Extracts plain text from a CV document without using AI.
     */
    @PostMapping(value = "/extract",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Extracts text from CV",
            description = "Extracts plain text from a CV document in PDF, DOC or DOCX format without using AI"
    )
    @ApiResponse(responseCode = "200", description = Constants.SWAGGER_OK,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CvExtractResponse.class)
            )
    )
    @ApiResponse(responseCode = "400", description = Constants.SWAGGER_BAD_REQUEST,
            content = @Content()
    )
    @ApiResponse(responseCode = "401", description = Constants.SWAGGER_UNAUTHORIZED,
            content = @Content()
    )
    @ApiResponse(responseCode = "415", description = Constants.SWAGGER_UNSUPPORTED_MEDIA_TYPE,
            content = @Content()
    )
    @ApiResponse(responseCode = "500", description = Constants.SWAGGER_INTERNAL_SERVER_ERROR,
            content = @Content()
    )
    public ResponseEntity<CvExtractResponse> extractText(@RequestPart("file") final MultipartFile file) throws IOException, TikaException {
        return ResponseEntity.status(HttpStatus.OK).body(
                new CvExtractResponse(cvExtractionService.extractText(file))
        );
    }

    /**
     * Checks a CV document for hidden or suspicious text without using AI.
     *
     * @param file original CV document
     * @return suspicious content analysis
     */
    @PostMapping(value = "/check",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Checks CV for suspicious content",
            description = "Checks the original PDF, DOC or DOCX CV document for hidden or suspicious text without using AI"
    )
    @ApiResponse(responseCode = "200",
            description = Constants.SWAGGER_OK,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CvCheckResponse.class)
            )
    )
    @ApiResponse(responseCode = "400",
            description = Constants.SWAGGER_BAD_REQUEST,
            content = @Content()
    )
    @ApiResponse(responseCode = "401",
            description = Constants.SWAGGER_UNAUTHORIZED,
            content = @Content()
    )
    @ApiResponse(responseCode = "415",
            description = Constants.SWAGGER_UNSUPPORTED_MEDIA_TYPE,
            content = @Content()
    )
    @ApiResponse(responseCode = "500",
            description = Constants.SWAGGER_INTERNAL_SERVER_ERROR,
            content = @Content()
    )
    public ResponseEntity<CvCheckResponse> check(@RequestPart("file") final MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(
                cvCheckService.check(file)
        );
    }

    /**
     * Parses extracted CV text into structured CV data using AI.
     *
     * @param request request containing extracted CV text
     * @return structured CV data
     */
    @PostMapping(value = "/parse",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Parses CV text using AI",
            description = "Converts extracted CV text into structured JSON using AI"
    )
    @ApiResponse(responseCode = "200",
            description = Constants.SWAGGER_OK,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CvParseResponse.class)
            )
    )
    @ApiResponse(responseCode = "400",
            description = Constants.SWAGGER_BAD_REQUEST,
            content = @Content()
    )
    @ApiResponse(responseCode = "401",
            description = Constants.SWAGGER_UNAUTHORIZED,
            content = @Content()
    )
    @ApiResponse(responseCode = "429",
            description = Constants.SWAGGER_TOO_MANY_REQUESTS,
            content = @Content()
    )
    @ApiResponse(responseCode = "500",
            description = Constants.SWAGGER_INTERNAL_SERVER_ERROR,
            content = @Content()
    )
    @ApiResponse(responseCode = "502",
            description = Constants.SWAGGER_BAD_GATEWAY,
            content = @Content()
    )
    @ApiResponse(responseCode = "503",
            description = Constants.SWAGGER_SERVICE_UNAVAILABLE,
            content = @Content()
    )
    public ResponseEntity<CvParseResponse> parse(@Valid @RequestBody final CvParseRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                cvParseService.parse(request.text())
        );
    }
}