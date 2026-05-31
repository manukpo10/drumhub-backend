package com.drumhub.notification.web;

import com.drumhub.common.web.ApiResponse;
import com.drumhub.notification.dto.NotificationResponse;
import com.drumhub.notification.dto.UnreadCountResponse;
import com.drumhub.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<Page<NotificationResponse>> getNotifications(
            @ParameterObject Pageable pageable,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.ok(notificationService.getNotifications(userDetails.getUsername(), pageable));
    }

    @GetMapping("/unread-count")
    public ApiResponse<UnreadCountResponse> getUnreadCount(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.ok(notificationService.getUnreadCount(userDetails.getUsername()));
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllRead(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        notificationService.markAllRead(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
