package cz.jpmad.cvinsight.api.cv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO for response with suspicious CV content analysis.
 */
public record CvCheckResponse(

        @JsonProperty("suspicious")
        boolean suspicious,

        @JsonProperty("issues")
        List<CvIssueResponse> issues
) {
}