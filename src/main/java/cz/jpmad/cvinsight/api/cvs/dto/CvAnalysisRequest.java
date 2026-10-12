package cz.jpmad.cvinsight.api.cvs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * Request for AI-based candidate analysis against a job offer.
 */
public record CvAnalysisRequest(

        @NotBlank
        @JsonProperty("jobOfferText")
        String jobOfferText

) {
}