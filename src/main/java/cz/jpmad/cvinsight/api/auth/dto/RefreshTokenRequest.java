package cz.jpmad.cvinsight.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for issuing a new access token from refresh token.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RefreshTokenRequest(
        @JsonProperty("refreshToken")
        @NotBlank(message = "Refresh token must not be blank")
        String refreshToken
) {
}
