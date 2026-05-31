package com.drumhub.groove.dto;

import java.time.Instant;

public record FavoriteResponse(
        Long grooveId,
        String grooveSlug,
        String grooveTitle,
        String authorUsername,
        String genre,
        Integer bpm,
        String level,
        Instant favoritedAt
) {}
