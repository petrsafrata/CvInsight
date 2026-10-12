package cz.jpmad.cvinsight.api.info;

import cz.jpmad.cvinsight.api.ApiEndpoints;
import cz.jpmad.cvinsight.api.Constants;
import cz.jpmad.cvinsight.api.info.dto.HtmlResponse;
import cz.jpmad.cvinsight.api.info.dto.VersionResponse;
import cz.jpmad.cvinsight.service.info.InfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(ApiEndpoints.INFO_ENDPOINT)
@RequiredArgsConstructor
@Tag(name = "Info", description = "Basic information about the application – version, release notes, ToS, privacy policy")
public class InfoController {

    private final InfoService info;

    @GetMapping("/version")
    @Operation(summary = "Returns the application version", description = "Returns the current version of the application as specified in the VERSION file")
    @ApiResponse(responseCode = "200", description = Constants.SWAGGER_OK,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = VersionResponse.class)))
    @ApiResponse(responseCode = "500", description = Constants.SWAGGER_INTERNAL_SERVER_ERROR, content = @Content())
    public ResponseEntity<VersionResponse> getVersion() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(new VersionResponse(info.getProjectVersion()));
    }

    @GetMapping("/release-notes")
    @Operation(summary = "Returns release notes as HTML", description = "Returns the release notes as specified in the RELEASE_NOTES file, formatted as HTML")
    @ApiResponse(responseCode = "200", description = Constants.SWAGGER_OK,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = HtmlResponse.class)))
    @ApiResponse(responseCode = "500", description = Constants.SWAGGER_INTERNAL_SERVER_ERROR, content = @Content())
    public ResponseEntity<HtmlResponse> getReleaseNotes() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(new HtmlResponse(info.getReleaseNotes()));
    }

    @GetMapping("/terms-of-service")
    @Operation(summary = "Returns the terms of use as HTML", description = "Returns the terms of use as specified in the TERMS_OF_SERVICE file, formatted as HTML")
    @ApiResponse(responseCode = "200", description = Constants.SWAGGER_OK,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = HtmlResponse.class)))
    @ApiResponse(responseCode = "500", description = Constants.SWAGGER_INTERNAL_SERVER_ERROR, content = @Content())
    public ResponseEntity<HtmlResponse> getToS() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(new HtmlResponse(info.getTermsOfService()));
    }

    @GetMapping("/privacy-policy")
    @Operation(summary = "Returns the privacy policy as HTML", description = "Returns the privacy policy as specified in the PRIVACY_POLICY file, formatted as HTML")
    @ApiResponse(responseCode = "200", description = Constants.SWAGGER_OK,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = HtmlResponse.class)))
    @ApiResponse(responseCode = "500", description = Constants.SWAGGER_INTERNAL_SERVER_ERROR, content = @Content())
    public ResponseEntity<HtmlResponse> getPrivacyPolicy() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(new HtmlResponse(info.getPrivacyPolicy()));
    }
}
