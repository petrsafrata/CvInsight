package cz.jpmad.cvinsight.api.cv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for CV text parsing request.
 */
public record CvParseRequest(

        @NotBlank
        @JsonProperty("text")
        String text
) {
}