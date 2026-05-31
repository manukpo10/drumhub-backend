package com.drumhub.genre.web;

import com.drumhub.common.web.ApiResponse;
import com.drumhub.genre.dto.GenreDetailResponse;
import com.drumhub.genre.dto.GenreResponse;
import com.drumhub.genre.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
@Tag(name = "Genres", description = "Read-only catalog of drum genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    @Operation(summary = "List all active genres")
    public ResponseEntity<ApiResponse<List<GenreResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(genreService.findAll()));
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Get genre detail by slug")
    public ResponseEntity<ApiResponse<GenreDetailResponse>> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.ok(genreService.findBySlug(slug)));
    }
}
