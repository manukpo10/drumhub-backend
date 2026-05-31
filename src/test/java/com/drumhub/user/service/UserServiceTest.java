package com.drumhub.user.service;

import com.drumhub.common.exception.ConflictException;
import com.drumhub.security.JwtService;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.dto.RegisterRequest;
import com.drumhub.user.dto.UserResponse;
import com.drumhub.user.mapper.UserMapper;
import com.drumhub.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    private static final RegisterRequest VALID_REQUEST = new RegisterRequest(
            "bonham",
            "John Bonham",
            "bonham@drumhub.com",
            "password123",
            "bonham"
    );

    @Test
    void register_whenUsernameExists_throwsConflictException() {
        when(userRepository.existsByUsername("bonham")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(VALID_REQUEST))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("bonham");

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_whenEmailExists_throwsConflictException() {
        when(userRepository.existsByUsername("bonham")).thenReturn(false);
        when(userRepository.existsByEmail("bonham@drumhub.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(VALID_REQUEST))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("bonham@drumhub.com");

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_whenValid_returnsUserResponse() {
        when(userRepository.existsByUsername("bonham")).thenReturn(false);
        when(userRepository.existsByEmail("bonham@drumhub.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed_password");

        User savedUser = User.builder()
                .username("bonham")
                .name("John Bonham")
                .email("bonham@drumhub.com")
                .passwordHash("hashed_password")
                .avatarSeed("bonham")
                .plan(Plan.FREE)
                .init("J")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse expectedResponse = new UserResponse(
                null, "bonham", "John Bonham", null, "bonham", null, "J", "free", Instant.now()
        );
        when(userMapper.toResponse(savedUser)).thenReturn(expectedResponse);

        UserResponse result = userService.register(VALID_REQUEST);

        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("bonham");
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("password123");
    }
}
