package com.drumhub.notification.dto;

import com.drumhub.notification.domain.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateNotificationRequest(
        @NotBlank String recipientUsername,
        @NotNull NotificationType type,
        @NotBlank String triggeredByUsername,
        String grooveSlug,
        String grooveTitle,
        String snippet
) {}
