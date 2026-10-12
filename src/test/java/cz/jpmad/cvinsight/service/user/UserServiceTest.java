package cz.jpmad.cvinsight.service.user;

import cz.jpmad.cvinsight.model.user.User;
import cz.jpmad.cvinsight.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);

        userService = new UserService(
                userRepository,
                mock(PasswordEncoder.class)
        );
    }

    @Test
    void loadUserByUsernameShouldReturnUser() {
        User user = mock(User.class);

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        var result = userService.loadUserByUsername("admin");

        assertSame(user, result);

        verify(userRepository).findByUsername("admin");
    }

    @Test
    void loadUserByUsernameShouldThrowWhenUserNotFound() {
        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("unknown")
        );
    }

    @Test
    void getByUsernameShouldReturnUser() {
        User user = mock(User.class);

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        User result = userService.getByUsername("admin");

        assertSame(user, result);
    }

    @Test
    void getByUsernameShouldThrowWhenUserNotFound() {
        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> userService.getByUsername("unknown")
        );
    }
}