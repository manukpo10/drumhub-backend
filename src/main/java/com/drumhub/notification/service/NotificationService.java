package com.drumhub.notification.service;

import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.notification.domain.Notification;
import com.drumhub.notification.dto.CreateNotificationRequest;
import com.drumhub.notification.dto.NotificationResponse;
import com.drumhub.notification.dto.UnreadCountResponse;
import com.drumhub.notification.mapper.NotificationMapper;
import com.drumhub.notification.repository.NotificationRepository;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotifications(String currentUsername, Pageable pageable) {
        User user = findByUsername(currentUsername);
        return notificationRepository
                .findByRecipientIdAndActivoTrueOrderByCreatedAtDesc(user.getId(), pageable)
                .map(notificationMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public UnreadCountResponse getUnreadCount(String currentUsername) {
        User user = findByUsername(currentUsername);
        long count = notificationRepository.countByRecipientIdAndReadFalseAndActivoTrue(user.getId());
        return new UnreadCountResponse(count);
    }

    @Transactional
    public void markAllRead(String currentUsername) {
        User user = findByUsername(currentUsername);
        notificationRepository.markAllReadByRecipientId(user.getId());
    }

    @Transactional
    public NotificationResponse push(CreateNotificationRequest request) {
        User recipient = userRepository.findByUsername(request.recipientUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + request.recipientUsername()));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .type(request.type())
                .triggeredBy(request.triggeredByUsername())
                .grooveSlug(request.grooveSlug())
                .grooveTitle(request.grooveTitle())
                .snippet(request.snippet())
                .build();

        return notificationMapper.toResponse(notificationRepository.save(notification));
    }

    private User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}
