package com.drumhub.security;

import com.drumhub.genre.service.GenreService;
import com.drumhub.genre.web.GenreController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Security-rule slice: loads the real {@link SecurityConfig} filter chain (filters ON)
 * against a representative public read endpoint, without booting the full application.
 *
 * <p>Guards the regression where uptime monitors and caches issuing {@code HEAD} requests
 * received {@code 403} because the authorization rules only permitted {@code GET}.
 * Per RFC 7231 a HEAD must be allowed wherever the matching GET is allowed.
 */
@WebMvcTest(controllers = GenreController.class)
@Import({SecurityConfig.class, com.drumhub.common.exception.GlobalExceptionHandler.class})
class PublicEndpointHttpMethodTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    // Collaborators SecurityConfig needs; the JWT filter is stubbed to pass requests through
    // so unauthenticated requests reach the authorization rules.
    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void passThroughJwtFilter() throws Exception {
        doAnswer(invocation -> {
            ServletRequest request = invocation.getArgument(0);
            ServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response);
            return null;
        }).when(jwtAuthFilter).doFilter(any(), any(), any());
    }

    @Test
    void get_publicGenres_isPermittedForAnonymous() throws Exception {
        when(genreService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk());
    }

    @Test
    void head_publicGenres_isPermittedForAnonymous() throws Exception {
        when(genreService.findAll()).thenReturn(List.of());

        // HEAD must mirror GET on a public endpoint — this is what uptime monitors send.
        mockMvc.perform(head("/api/genres"))
                .andExpect(status().isOk());
    }

    @Test
    void head_protectedMeRoute_isStillForbiddenForAnonymous() throws Exception {
        // The new HEAD allowance must NOT leak protected routes: the authenticated /me
        // matchers are declared first, so an anonymous HEAD is still denied.
        mockMvc.perform(head("/api/users/me"))
                .andExpect(status().isForbidden());
    }
}
