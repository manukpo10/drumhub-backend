package com.drumhub.user.dto;

public record FollowStatusResponse(
    String followeeUsername,
    boolean following,
    long followerCount
) {}
