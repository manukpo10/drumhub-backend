package com.drumhub.notification.dto;

import java.time.Instant;

public record NotificationResponse(
        Long id,
        String type,
        String triggeredBy,
        boolean read,
        String grooveSlug,
        String grooveTitle,
        String snippet,
        Instant createdAt
) {}
