package cz.jpmad.cvinsight.api.cv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a response containing information about a language and its proficiency level.
 */
public record CvLanguageResponse(

        @JsonProperty("language")
        String language,

        @JsonProperty("level")
        String level
) {
}