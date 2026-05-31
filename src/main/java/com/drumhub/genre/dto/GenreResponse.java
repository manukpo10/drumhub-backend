package com.drumhub.genre.dto;

public record GenreResponse(
        Long id,
        String name,
        String slug,
        String icon,
        String color,
        String level,
        String timeSig,
        Integer bpmMin,
        Integer bpmMax,
        long grooveCount
) {}
