package cz.jpmad.cvinsight.api.info.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for response with HTML content.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record HtmlResponse(

        @JsonProperty("content")
        String content
) {
}
