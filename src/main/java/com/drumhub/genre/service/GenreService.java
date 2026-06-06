package com.drumhub.genre.service;

import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.genre.domain.Genre;
import com.drumhub.genre.dto.GenreDetailResponse;
import com.drumhub.genre.dto.GenreResponse;
import com.drumhub.genre.mapper.GenreMapper;
import com.drumhub.genre.repository.GenreRepository;
import com.drumhub.groove.repository.GrooveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GrooveRepository grooveRepository;
    private final GenreMapper genreMapper;

    @Transactional(readOnly = true)
    public List<GenreResponse> findAll() {
        List<Genre> genres = genreRepository.findByActivoTrueOrderByNameAsc();

        // Real active-groove count per genre, in a single grouped query (no N+1).
        Map<Long, Long> countsByGenreId = new HashMap<>();
        for (Object[] row : grooveRepository.countActiveGroupedByGenre()) {
            countsByGenreId.put((Long) row[0], (Long) row[1]);
        }

        return genres.stream()
                .map(genre -> genreMapper.toResponse(genre, countsByGenreId.getOrDefault(genre.getId(), 0L)))
                .toList();
    }

    @Transactional(readOnly = true)
    public GenreDetailResponse findBySlug(String slug) {
        Genre genre = genreRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found: " + slug));
        long grooveCount = grooveRepository.countActiveByGenreId(genre.getId());
        return genreMapper.toDetailResponse(genre, grooveCount);
    }
}
