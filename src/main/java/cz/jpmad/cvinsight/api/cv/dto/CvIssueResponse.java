package cz.jpmad.cvinsight.api.cv.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.jpmad.cvinsight.model.cv.CvIssueType;

/**
 * DTO describing suspicious content detected in a CV document.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CvIssueResponse(

        @JsonProperty("type")
        CvIssueType type,

        @JsonProperty("text")
        String text,

        @JsonProperty("fontSize")
        Float fontSize
) {
}