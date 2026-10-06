package cz.jpmad.springprojecttemplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * CORS konfigurace pro komunikaci s frontendem
 */
@Configuration
public class WebConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration config = new CorsConfiguration();

        // Povolené origins – přizpůsobte dle prostředí
        // Porty 5173/5174 jsou běžné pro React/Vite dev server, port 80 pro produkci v Apache
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

        // Povolené HTTP metody
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // Povolené hlavičky
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        // Povolení cookies / Authorization hlaviček
        config.setAllowCredentials(true);

        // Cache preflight odpovědí (v sekundách)
        config.setMaxAge(3600L);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
