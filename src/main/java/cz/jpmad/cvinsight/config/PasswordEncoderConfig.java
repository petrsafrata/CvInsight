package cz.jpmad.cvinsight.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuring PasswordEncoder to avoid circular dependency
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    TextEncryptor connectionPasswordEncryptor(
            @Value("${SECURITY_CONNECTION_PASSWORD_SECRET:dev-connection-secret}") final String secret,
            @Value("${SECURITY_CONNECTION_PASSWORD_SALT:0123456789abcdef}") final String salt
    ) {
        return Encryptors.text(secret, salt);
    }
}
