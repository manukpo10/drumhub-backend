package com.drumhub.user.dto;

import java.time.Instant;

public record UserResponse(
        Long id,
        String username,
        String name,
        String bio,
        String avatarSeed,
        String avatarUrl,
        String color,
        String init,
        String plan,
        Instant createdAt
) {}
