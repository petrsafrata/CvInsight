package cz.jpmad.cvinsight.service.user;

import cz.jpmad.cvinsight.model.user.User;
import cz.jpmad.cvinsight.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User management and authentication service.
 * Implements UserDetailsService for integration with Spring Security.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves the user by username for authentication.
     *
     * @throws UsernameNotFoundException if the user does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final @NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    /**
     * Returns the user by username.
     *
     * @throws java.util.NoSuchElementException if the user does not exist
     */
    @Transactional(readOnly = true)
    public User getByUsername(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new java.util.NoSuchElementException("User not found: " + username));
    }
}
