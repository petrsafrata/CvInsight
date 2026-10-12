package cz.jpmad.cvinsight.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Spring configuration for HTTP clients.
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates a WebClient builder used by HTTP API clients.
     *
     * @return configured WebClient builder; never {@code null}
     */
    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}