package com.drumhub.user.dto;

public record UserSummaryResponse(
    Long id,
    String username,
    String name,
    String avatarSeed,
    String color,
    String init,
    String plan,
    long followerCount,
    long followingCount
) {}
