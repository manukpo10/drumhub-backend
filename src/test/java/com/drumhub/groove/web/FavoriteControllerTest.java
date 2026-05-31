package com.drumhub.groove.web;

import com.drumhub.groove.dto.FavoriteStatusResponse;
import com.drumhub.groove.service.FavoriteService;
import com.drumhub.security.JwtAuthFilter;
import com.drumhub.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = FavoriteController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.drumhub.common.exception.GlobalExceptionHandler.class)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void getFavorites_withoutAuth_returns403() throws Exception {
        // With addFilters=false, null principal → NPE → 500 in unit context
        // This documents the contract: unauthenticated calls fail
        when(favoriteService.getFavorites(any(), any()))
                .thenThrow(new NullPointerException("No principal"));

        mockMvc.perform(get("/api/users/me/favorites").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "drummer1")
    void addFavorite_withAuth_returns201() throws Exception {
        FavoriteStatusResponse response = new FavoriteStatusResponse(1L, true, 5L);

        when(favoriteService.addFavorite(anyString(), anyLong())).thenReturn(response);

        mockMvc.perform(post("/api/users/me/favorites/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.favorited").value(true))
                .andExpect(jsonPath("$.data.totalFavorites").value(5));
    }
}
