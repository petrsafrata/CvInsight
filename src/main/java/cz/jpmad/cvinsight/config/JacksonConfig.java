package cz.jpmad.cvinsight.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for Jackson JSON mapping.
 * <p>
 * It provides a default {@link ObjectMapper} only when no other mapper bean
 * is already registered in the application context.
 * </p>
 */
@Configuration
public class JacksonConfig {

    /**
     * Creates a default Jackson {@link ObjectMapper} configured with discovered modules.
     * <p>Side effects: registers a singleton mapper bean in the Spring context when missing.</p>
     *
     * @return configured object mapper; never {@code null}
     */
    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }
}
