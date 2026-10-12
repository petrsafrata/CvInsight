package cz.jpmad.cvinsight.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.jpmad.cvinsight.model.user.User;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO for response with user data.
 * Never contains password or password hash.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("username")
        String username,

        @JsonProperty("createdAt")
        Instant createdAt
) {
    public static UserResponse from(final User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getCreatedAt()
        );
    }
}
