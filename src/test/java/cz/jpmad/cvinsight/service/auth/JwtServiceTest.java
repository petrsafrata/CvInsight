package cz.jpmad.cvinsight.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(
                "test-secret-key-with-at-least-32-characters-123456",
                3600,
                86400
        );

        jwtService.init();

        userDetails = User.withUsername("testuser")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    void generateAccessTokenShouldContainUsername() {
        String token = jwtService.generateAccessToken(userDetails);

        assertNotNull(token);
        assertEquals("testuser", jwtService.extractUsername(token));
    }

    @Test
    void accessTokenShouldBeValid() {
        String token = jwtService.generateAccessToken(userDetails);

        assertTrue(jwtService.isAccessTokenValid(token, userDetails));
    }

    @Test
    void refreshTokenShouldNotBeValidAsAccessToken() {
        String token = jwtService.generateRefreshToken(userDetails);

        assertFalse(jwtService.isAccessTokenValid(token, userDetails));
    }

    @Test
    void accessTokenShouldNotBeValidAsRefreshToken() {
        String token = jwtService.generateAccessToken(userDetails);

        assertFalse(jwtService.isRefreshTokenValid(token, userDetails));
    }

    @Test
    void tokenShouldNotBeValidForDifferentUser() {
        String token = jwtService.generateAccessToken(userDetails);

        UserDetails otherUser = User.withUsername("otheruser")
                .password("password")
                .roles("USER")
                .build();

        assertFalse(jwtService.isAccessTokenValid(token, otherUser));
    }

    @Test
    void expiredAccessTokenShouldBeRejected() {
        JwtService expiredJwtService = new JwtService(
                "test-secret-key-with-at-least-32-characters-123456",
                -60,
                86400
        );

        expiredJwtService.init();

        String token = expiredJwtService.generateAccessToken(userDetails);

        assertThrows(
                io.jsonwebtoken.ExpiredJwtException.class,
                () -> expiredJwtService.isAccessTokenValid(
                        token,
                        userDetails
                )
        );
    }
}