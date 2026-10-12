package cz.jpmad.cvinsight.api.cv.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the response for a CV education entry.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CvEducationResponse(

        @JsonProperty("school")
        String school,

        @JsonProperty("field")
        String field,

        @JsonProperty("degree")
        String degree
) {
}