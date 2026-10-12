package cz.jpmad.cvinsight.api.cv.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a response containing work experience information for a CV.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CvWorkExperienceResponse(

        @JsonProperty("position")
        String position,

        @JsonProperty("company")
        String company,

        @JsonProperty("from")
        String from,

        @JsonProperty("to")
        String to
) {
}