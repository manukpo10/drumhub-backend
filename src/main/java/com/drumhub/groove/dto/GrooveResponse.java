package com.drumhub.groove.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record GrooveResponse(
        Long id,
        String slug,
        String title,
        String authorUsername,
        String authorName,
        String genre,
        String genreSlug,
        Integer bpm,
        String level,
        Long likes,
        Long plays,
        Boolean featured,
        List<String> tags,
        String description,
        Map<String, List<Integer>> pattern,
        String timeSig,
        Integer bars,
        String kit,
        Instant createdAt
) {}
