package cz.jpmad.cvinsight.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response payload with renewed access and refresh tokens.
 */
public record RefreshTokenResponse(
        @JsonProperty("accessToken")
        String accessToken,

        @JsonProperty("refreshToken")
        String refreshToken
) {
}
