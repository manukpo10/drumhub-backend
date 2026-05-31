package com.drumhub.groove.dto;

import java.time.Instant;

public record CommentResponse(
        Long id,
        String authorUsername,
        String authorName,
        String authorAvatarSeed,
        String text,
        Instant createdAt
) {}
