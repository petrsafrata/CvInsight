package cz.jpmad.cvinsight.api.cv.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for response with extracted CV data.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CvExtractResponse(

        @JsonProperty("text")
        String text
) {
}
