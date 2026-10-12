package cz.jpmad.cvinsight.api.cvs;

import cz.jpmad.cvinsight.api.ApiEndpoints;
import cz.jpmad.cvinsight.api.cvs.dto.CvAnalysisRequest;
import cz.jpmad.cvinsight.api.cvs.dto.CvAnalysisResponse;
import cz.jpmad.cvinsight.api.Constants;
import cz.jpmad.cvinsight.api.cvs.dto.CvsCreateRequest;
import cz.jpmad.cvinsight.api.cvs.dto.CvsResponse;
import cz.jpmad.cvinsight.service.cvs.CvAnalysisService;
import cz.jpmad.cvinsight.service.cvs.CvsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for operations with stored CVs.
 */
@RestController
@RequestMapping(ApiEndpoints.CVS_ENDPOINT)
@RequiredArgsConstructor
@Tag(name = "Stored CVs", description = "Operations with stored CVs and candidate analysis")
public class CvsController {

    private final CvAnalysisService cvAnalysisService;
    private final CvsService cvsService;

    /**
     * Stores a structured CV.
     *
     * @param request structured CV data
     * @return stored CV
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Stores structured CV",
            description = "Stores structured CV data in the database"
    )
    @ApiResponse(responseCode = "201", description = Constants.SWAGGER_CREATED,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CvsResponse.class)
            )
    )
    @ApiResponse(responseCode = "400", description = Constants.SWAGGER_BAD_REQUEST,
            content = @Content()
    )
    @ApiResponse(responseCode = "401", description = Constants.SWAGGER_UNAUTHORIZED,
            content = @Content()
    )
    @ApiResponse(responseCode = "500", description = Constants.SWAGGER_INTERNAL_SERVER_ERROR,
            content = @Content()
    )
    public ResponseEntity<CvsResponse> create(
            @Valid @RequestBody final CvsCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cvsService.create(request));
    }

    /**
     * Returns all stored CVs.
     *
     * @return list of stored CVs
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns stored CVs", description = "Returns all structured CVs stored in the database")
    @ApiResponse(responseCode = "200", description = Constants.SWAGGER_OK)
    @ApiResponse(responseCode = "401", description = Constants.SWAGGER_UNAUTHORIZED,
            content = @Content()
    )
    public ResponseEntity<List<CvsResponse>> findAll() {
        return ResponseEntity.ok(cvsService.findAll());
    }

    /**
     * Returns one stored CV.
     *
     * @param id CV identifier
     * @return stored CV
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns stored CV", description = "Returns a structured CV by its identifier")
    @ApiResponse(responseCode = "200", description = Constants.SWAGGER_OK,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CvsResponse.class)
            )
    )
    @ApiResponse(responseCode = "401", description = Constants.SWAGGER_UNAUTHORIZED,
            content = @Content()
    )
    @ApiResponse(responseCode = "404", description = Constants.SWAGGER_NOT_FOUND,
            content = @Content()
    )
    public ResponseEntity<CvsResponse> findById(@PathVariable final UUID id) {
        return ResponseEntity.ok(cvsService.getById(id));
    }

    /**
     * Deletes a stored CV.
     *
     * @param id CV identifier
     * @return empty response
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes stored CV", description = "Deletes a structured CV from the database")
    @ApiResponse(responseCode = "204", description = Constants.SWAGGER_NO_CONTENT,
            content = @Content()
    )
    @ApiResponse(responseCode = "401", description = Constants.SWAGGER_UNAUTHORIZED,
            content = @Content()
    )
    @ApiResponse(responseCode = "404", description = Constants.SWAGGER_NOT_FOUND,
            content = @Content()
    )
    public ResponseEntity<Void> delete(@PathVariable final UUID id) {
        cvsService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Analyzes a stored CV against a job offer using AI.
     *
     * @param id      identifier of the stored CV
     * @param request job offer used for candidate analysis
     * @return candidate analysis
     */
    @PostMapping(value = "/{id}/analysis", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Analyzes candidate against job offer",
            description = """
                    Loads a stored CV and compares its professionally relevant
                    information with the supplied job offer using AI.
                    """
    )
    @ApiResponse(responseCode = "200", description = Constants.SWAGGER_OK,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = CvAnalysisResponse.class
                    )
            )
    )
    @ApiResponse(responseCode = "400", description = Constants.SWAGGER_BAD_REQUEST,
            content = @Content()
    )
    @ApiResponse(responseCode = "401", description = Constants.SWAGGER_UNAUTHORIZED,
            content = @Content()
    )
    @ApiResponse(responseCode = "404", description = Constants.SWAGGER_NOT_FOUND,
            content = @Content()
    )
    @ApiResponse(responseCode = "500", description = Constants.SWAGGER_INTERNAL_SERVER_ERROR,
            content = @Content()
    )
    public ResponseEntity<CvAnalysisResponse> analyze(
            @PathVariable final UUID id,
            @Valid @RequestBody final CvAnalysisRequest request
    ) {
        return ResponseEntity.ok(cvAnalysisService.analyze(id, request.jobOfferText())
        );
    }
}