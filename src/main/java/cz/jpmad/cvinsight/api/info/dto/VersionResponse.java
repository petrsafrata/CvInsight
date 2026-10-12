package cz.jpmad.cvinsight.api.info.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for response with application version.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record VersionResponse(

        @JsonProperty("version")
        String version
) {
}
