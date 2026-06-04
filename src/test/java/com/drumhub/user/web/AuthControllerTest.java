package com.drumhub.user.web;

import com.drumhub.common.exception.ConflictException;
import com.drumhub.security.JwtAuthFilter;
import com.drumhub.security.JwtService;
import com.drumhub.user.dto.AuthResponse;
import com.drumhub.user.dto.LoginRequest;
import com.drumhub.user.dto.RegisterRequest;
import com.drumhub.user.dto.UserResponse;
import com.drumhub.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @WebMvcTest slice for AuthController.
 * Filters are disabled (addFilters=false) to isolate controller and exception-handling
 * logic from security filter chain wiring complexity.
 */
@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.drumhub.common.exception.GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    private static final UserResponse SAMPLE_USER = new UserResponse(
            1L, "bonham", "John Bonham", null, "bonham", null, "#e8ff00", "J", "free", Instant.now()
    );

    @Test
    void register_whenValid_returns201() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "bonham", "John Bonham", "bonham@drumhub.com", "password123", "bonham"
        );
        when(userService.register(any(RegisterRequest.class))).thenReturn(SAMPLE_USER);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("bonham"));
    }

    @Test
    void register_whenUsernameTaken_returns409() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "bonham", "John Bonham", "bonham@drumhub.com", "password123", "bonham"
        );
        when(userService.register(any(RegisterRequest.class)))
                .thenThrow(new ConflictException("Username already taken: bonham"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void login_whenCredentialsValid_returns200WithToken() throws Exception {
        LoginRequest request = new LoginRequest("bonham", "password123");
        AuthResponse authResponse = AuthResponse.of("jwt.token.here", SAMPLE_USER);
        when(userService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("jwt.token.here"))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.user.username").value("bonham"));
    }
}
