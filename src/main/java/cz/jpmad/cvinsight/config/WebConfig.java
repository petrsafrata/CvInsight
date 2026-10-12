package cz.jpmad.cvinsight.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * CORS configuration for communication with the frontend
 */
@Configuration
public class WebConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration config = new CorsConfiguration();

        // Origins enabled - adjust for environment
        // Ports 5173/5174 are common for React/Vite dev server, port 80 for production in Apache
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:5174",
                "http://127.0.0.1:5173",
                "http://127.0.0.1:5174",
                "http://localhost:80",
                "http://127.0.0.1:80",
                "http://localhost",
                "http://127.0.0.1"
        ));

        // Allowed HTTP methods
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // Allowed headers
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        // JWT is sent in Authorization header, cookie credentials are not needed.
        config.setAllowCredentials(false);

        // Cache preflight response
        config.setMaxAge(3600L);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // With context-path `/api` set, Spring compares paths without a prefix,
        // therefore we register CORS globally.
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
