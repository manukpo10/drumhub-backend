package com.drumhub.groove.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

public record UpdateGrooveRequest(
        @NotBlank @Size(max = 100) String title,
        @NotBlank String genre,
        @NotNull @Min(40) @Max(220) Integer bpm,
        @NotBlank String level,
        @Size(max = 500) String description,
        List<@Size(max = 50) String> tags,
        @NotNull Map<String, List<Integer>> pattern,
        String timeSig,
        @Min(1) @Max(4) Integer bars
) {}
