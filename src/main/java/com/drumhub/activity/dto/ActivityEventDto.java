package com.drumhub.activity.dto;

import java.time.Instant;

/**
 * A single event in the community activity feed.
 *
 * Types: "upload" | "comment" | "like" | "follow"
 *
 * - upload  : actor posted a groove (grooveTitle + grooveSlug populated)
 * - comment : actor commented on a groove (grooveTitle + grooveSlug populated)
 * - like    : actor liked a groove (grooveTitle + grooveSlug populated)
 * - follow  : actor started following targetUser (targetUser + targetUserName populated)
 */
public record ActivityEventDto(
        String type,
        String actor,
        String actorName,
        String grooveTitle,
        String grooveSlug,
        String targetUser,
        String targetUserName,
        Instant createdAt
) {}
