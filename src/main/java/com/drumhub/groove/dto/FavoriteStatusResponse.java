package com.drumhub.groove.dto;

public record FavoriteStatusResponse(
        Long grooveId,
        boolean favorited,
        long totalFavorites
) {}
