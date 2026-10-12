package cz.jpmad.cvinsight.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * Service for issuing and validating JWT access/refresh tokens.
 */
@Service
public class JwtService {

    private static final String CLAIM_TOKEN_TYPE = "type";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";

    private final String secret;
    private final long accessTokenTtlSeconds;
    private final long refreshTokenTtlSeconds;

    private SecretKey signingKey;

    /**
     * Creates a JWT service with configured secret and token lifetimes.
     *
     * @param secret signing secret
     * @param accessTokenTtlSeconds access token lifetime in seconds
     * @param refreshTokenTtlSeconds refresh token lifetime in seconds
     */
    public JwtService(@Value("${security.jwt.secret}") final String secret,
                      @Value("${security.jwt.accessTokenTtlSeconds}") final long accessTokenTtlSeconds,
                      @Value("${security.jwt.refreshTokenTtlSeconds}") final long refreshTokenTtlSeconds) {
        this.secret = secret;
        this.accessTokenTtlSeconds = accessTokenTtlSeconds;
        this.refreshTokenTtlSeconds = refreshTokenTtlSeconds;
    }

    /**
     * Initializes the signing key from the configured secret.
     */
    @PostConstruct
    void init() {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates an access token for the given user.
     *
     * @param userDetails authenticated user details
     * @return signed access token
     */
    public String generateAccessToken(final UserDetails userDetails) {
        return buildToken(userDetails.getUsername(), TOKEN_TYPE_ACCESS, accessTokenTtlSeconds);
    }

    /**
     * Generates a refresh token for the given user.
     *
     * @param userDetails authenticated user details
     * @return signed refresh token
     */
    public String generateRefreshToken(final UserDetails userDetails) {
        return buildToken(userDetails.getUsername(), TOKEN_TYPE_REFRESH, refreshTokenTtlSeconds);
    }

    /**
     * Extracts username (subject) from a token.
     *
     * @param token JWT token
     * @return username stored in the token
     */
    public String extractUsername(final String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Checks whether an access token is valid for the given user.
     *
     * @param token JWT token
     * @param userDetails expected user details
     * @return true if token is valid access token for the user
     */
    public boolean isAccessTokenValid(final String token, final UserDetails userDetails) {
        return isTokenValid(token, userDetails, TOKEN_TYPE_ACCESS);
    }

    /**
     * Checks whether a refresh token is valid for the given user.
     *
     * @param token JWT token
     * @param userDetails expected user details
     * @return true if token is valid refresh token for the user
     */
    public boolean isRefreshTokenValid(final String token, final UserDetails userDetails) {
        return isTokenValid(token, userDetails, TOKEN_TYPE_REFRESH);
    }

    /**
     * Validates token subject, type, and expiration.
     *
     * @param token JWT token
     * @param userDetails expected user details
     * @param expectedType expected token type
     * @return true if token matches all validation rules
     */
    private boolean isTokenValid(final String token, final UserDetails userDetails, final String expectedType) {
        final Claims claims = extractAllClaims(token);
        final String username = claims.getSubject();
        final String tokenType = claims.get(CLAIM_TOKEN_TYPE, String.class);
        return username != null
                && username.equals(userDetails.getUsername())
                && expectedType.equals(tokenType)
                && claims.getExpiration() != null
                && claims.getExpiration().after(new Date());
    }

    /**
     * Builds and signs a token for the given username and type.
     *
     * @param username token subject
     * @param tokenType token type claim value
     * @param ttlSeconds token lifetime in seconds
     * @return signed JWT token
     */
    private String buildToken(final String username, final String tokenType, final long ttlSeconds) {
        final Instant now = Instant.now();
        return Jwts.builder()
                .claims(Map.of(CLAIM_TOKEN_TYPE, tokenType))
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlSeconds)))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Parses token and returns all claims.
     *
     * @param token JWT token
     * @return parsed token claims
     */
    private Claims extractAllClaims(final String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
