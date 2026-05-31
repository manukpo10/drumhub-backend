package com.drumhub.genre.service;

import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.genre.domain.Genre;
import com.drumhub.genre.dto.GenreDetailResponse;
import com.drumhub.genre.dto.GenreResponse;
import com.drumhub.genre.mapper.GenreMapper;
import com.drumhub.genre.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Transactional(readOnly = true)
    public List<GenreResponse> findAll() {
        List<Genre> genres = genreRepository.findByActivoTrueOrderByNameAsc();
        return genreMapper.toResponseList(genres);
    }

    @Transactional(readOnly = true)
    public GenreDetailResponse findBySlug(String slug) {
        Genre genre = genreRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found: " + slug));
        return genreMapper.toDetailResponse(genre);
    }
}
