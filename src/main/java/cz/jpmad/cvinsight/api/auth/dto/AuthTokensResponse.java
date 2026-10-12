package cz.jpmad.cvinsight.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authentication response containing access and refresh token and user payload.
 */
public record AuthTokensResponse(
        @JsonProperty("accessToken")
        String accessToken,

        @JsonProperty("refreshToken")
        String refreshToken,

        @JsonProperty("user")
        UserResponse user
) {
}
