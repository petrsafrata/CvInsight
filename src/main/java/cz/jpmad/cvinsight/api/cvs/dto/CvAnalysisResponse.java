package cz.jpmad.cvinsight.api.cvs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Result of AI-based candidate analysis against a job offer.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CvAnalysisResponse(

        @JsonProperty("candidateSummary")
        String candidateSummary,

        @JsonProperty("matchingSkills")
        List<String> matchingSkills,

        @JsonProperty("missingSkills")
        List<String> missingSkills

) {
}