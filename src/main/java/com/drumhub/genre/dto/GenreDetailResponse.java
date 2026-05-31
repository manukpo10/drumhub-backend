package com.drumhub.genre.dto;

import java.util.List;

public record GenreDetailResponse(
        Long id,
        String name,
        String slug,
        String icon,
        String color,
        String level,
        String timeSig,
        Integer bpmMin,
        Integer bpmMax,
        String description,
        List<String> subgenres,
        List<String> related,
        long grooveCount
) {}
