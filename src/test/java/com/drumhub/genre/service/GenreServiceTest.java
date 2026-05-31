package com.drumhub.genre.service;

import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.genre.domain.Genre;
import com.drumhub.genre.dto.GenreDetailResponse;
import com.drumhub.genre.dto.GenreResponse;
import com.drumhub.genre.mapper.GenreMapper;
import com.drumhub.genre.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @InjectMocks
    private GenreService genreService;

    private static Genre buildGenre(String name, String slug) {
        return Genre.builder()
                .name(name)
                .slug(slug)
                .icon("🎸")
                .color("#e8ff00")
                .bpmMin(100)
                .bpmMax(160)
                .level("Intermedio")
                .timeSig("4/4")
                .description("Test description")
                .subgenres(List.of("Classic Rock"))
                .related(List.of("Metal"))
                .build();
    }

    @Test
    void findAll_returnsAllActiveGenres() {
        Genre rock = buildGenre("Rock", "rock");
        Genre funk = buildGenre("Funk", "funk");
        List<Genre> genres = List.of(funk, rock);

        GenreResponse rockResponse = new GenreResponse(null, "Rock", "rock", "🎸", "#e8ff00", "Intermedio", "4/4", 100, 160, 0L);
        GenreResponse funkResponse = new GenreResponse(null, "Funk", "funk", "🕺", "#ff6b00", "Avanzado", "4/4", 80, 120, 0L);

        when(genreRepository.findByActivoTrueOrderByNameAsc()).thenReturn(genres);
        when(genreMapper.toResponseList(genres)).thenReturn(List.of(funkResponse, rockResponse));

        List<GenreResponse> result = genreService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Funk");
        assertThat(result.get(1).name()).isEqualTo("Rock");
    }

    @Test
    void findBySlug_whenExists_returnsDetail() {
        Genre rock = buildGenre("Rock", "rock");
        GenreDetailResponse detail = new GenreDetailResponse(
                1L, "Rock", "rock", "🎸", "#e8ff00", "Intermedio", "4/4",
                100, 160, "Test description", List.of("Classic Rock"), List.of("Metal"), 0L
        );

        when(genreRepository.findBySlug("rock")).thenReturn(Optional.of(rock));
        when(genreMapper.toDetailResponse(rock)).thenReturn(detail);

        GenreDetailResponse result = genreService.findBySlug("rock");

        assertThat(result).isNotNull();
        assertThat(result.slug()).isEqualTo("rock");
        assertThat(result.grooveCount()).isZero();
    }

    @Test
    void findBySlug_whenNotFound_throwsResourceNotFoundException() {
        when(genreRepository.findBySlug("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> genreService.findBySlug("unknown"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("unknown");
    }
}
