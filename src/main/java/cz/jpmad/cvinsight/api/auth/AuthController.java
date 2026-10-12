package cz.jpmad.cvinsight.api.auth;

import cz.jpmad.cvinsight.api.ApiEndpoints;
import cz.jpmad.cvinsight.api.Constants;
import cz.jpmad.cvinsight.api.auth.dto.AuthTokensResponse;
import cz.jpmad.cvinsight.api.auth.dto.LoginRequest;
import cz.jpmad.cvinsight.api.auth.dto.UserResponse;
import cz.jpmad.cvinsight.model.user.User;
import cz.jpmad.cvinsight.service.auth.JwtService;
import cz.jpmad.cvinsight.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication.
 */
@RestController
@RequestMapping(ApiEndpoints.AUTH_ENDPOINT)
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Users authentication and token management")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    /**
     * User login. Validates credentials and returns user data.
     *
     * @param request The login request containing username and password.
     * @return ResponseEntity containing JWT access and refresh tokens along with user data.
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Returns JWT access and refresh tokens along with user data")
    @ApiResponse(responseCode = "200", description = Constants.SWAGGER_OK,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AuthTokensResponse.class)))
    @ApiResponse(responseCode = "400", description = Constants.SWAGGER_BAD_REQUEST, content = @Content())
    @ApiResponse(responseCode = "401", description = Constants.SWAGGER_UNAUTHORIZED, content = @Content())
    public ResponseEntity<AuthTokensResponse> login(@Valid @RequestBody final LoginRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var authentication = authenticationManager.authenticate(authToken);
        final User user = (User) authentication.getPrincipal();

        assert user != null;
        return ResponseEntity.status(HttpStatus.OK).body(new AuthTokensResponse(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user),
                UserResponse.from(user)
        ));
    }

    /**
     * Returns the currently authenticated user from JWT context.
     *
     * @param user The currently authenticated user injected by Spring Security.
     * @return ResponseEntity containing the user data.
     */
    @Operation(summary = "Returns current user", description = "Returns the currently authenticated user based on JWT access token")
    @ApiResponse(responseCode = "200", description = Constants.SWAGGER_OK,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "401", description = Constants.SWAGGER_UNAUTHORIZED, content = @Content())
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal final User user) {
        if (user == null) {
            throw new BadCredentialsException("Not authenticated");
        }
        return ResponseEntity.status(HttpStatus.OK).body(UserResponse.from(user));
    }

    /**
     * Logs out user on API level.
     * <p>
     * With stateless JWT this endpoint is informational only and returns 200.
     */
    @Operation(summary = "Logout user", description = "Logs out the user. With stateless JWT this endpoint is informational only and returns 200")
    @ApiResponse(responseCode = "200", description = Constants.SWAGGER_OK, content = @Content())
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
