package com.drumhub.genre.web;

import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.genre.dto.GenreDetailResponse;
import com.drumhub.genre.dto.GenreResponse;
import com.drumhub.genre.service.GenreService;
import com.drumhub.security.JwtAuthFilter;
import com.drumhub.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = GenreController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.drumhub.common.exception.GlobalExceptionHandler.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    private static final GenreResponse ROCK = new GenreResponse(
            1L, "Rock", "rock", "🎸", "#e8ff00", "Intermedio", "4/4", 100, 160, 0L
    );

    private static final GenreResponse FUNK = new GenreResponse(
            2L, "Funk", "funk", "🕺", "#ff6b00", "Avanzado", "4/4", 80, 120, 0L
    );

    @Test
    void getAll_returns200WithGenreList() throws Exception {
        when(genreService.findAll()).thenReturn(List.of(FUNK, ROCK));

        mockMvc.perform(get("/api/genres").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("Funk"))
                .andExpect(jsonPath("$.data[1].name").value("Rock"));
    }

    @Test
    void getBySlug_whenExists_returns200() throws Exception {
        GenreDetailResponse detail = new GenreDetailResponse(
                1L, "Rock", "rock", "🎸", "#e8ff00", "Intermedio", "4/4",
                100, 160, "Test description", List.of("Classic Rock"), List.of("Metal"), 0L
        );
        when(genreService.findBySlug("rock")).thenReturn(detail);

        mockMvc.perform(get("/api/genres/rock").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.slug").value("rock"))
                .andExpect(jsonPath("$.data.grooveCount").value(0));
    }

    @Test
    void getBySlug_whenNotFound_returns404() throws Exception {
        when(genreService.findBySlug("unknown"))
                .thenThrow(new ResourceNotFoundException("Genre not found: unknown"));

        mockMvc.perform(get("/api/genres/unknown").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
