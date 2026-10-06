package cz.jpmad.springprojecttemplate.config;

import cz.jpmad.springprojecttemplate.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Konfigurace Spring Security pro REST API.
 * - Používá HTTP Basic autentizaci.
 * - Session-based sessions s limitem 1 session.
 * - Role-based autorizace pro admin endpointy.
 * - CORS konfigurace z WebConfig.
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Bean
    DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(final AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                // Stateless REST API – CSRF není potřeba
                .csrf(AbstractHttpConfigurer::disable)
                // CORS – konfigurace z WebConfig
                .cors(Customizer.withDefaults())
                // Session management
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).maximumSessions(1))
                // HTTP Basic autentizace pro REST API
                .httpBasic(Customizer.withDefaults())
                // Autorizace endpointů
                .authorizeHttpRequests(auth -> auth
                        // Veřejné endpointy
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/info/version").permitAll()
                        // Swagger / OpenAPI
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll() // Pozdeji .hasRole("ADMIN") pro omezení přístupu pouze pro adminy
                        // Actuator health
                        .requestMatchers("/actuator/").permitAll() // Pozdeji .hasRole("ADMIN") pro omezení přístupu pouze pro adminy
                        // Admin endpointy
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Vše ostatní vyžaduje autentizaci
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider(passwordEncoder))
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .rememberMe(rem -> rem
                        .tokenValiditySeconds(120) // 2 minuty
                );

        return http.build();
    }
}
