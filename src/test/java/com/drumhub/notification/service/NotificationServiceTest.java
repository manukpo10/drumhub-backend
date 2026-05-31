package com.drumhub.notification.service;

import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.notification.domain.Notification;
import com.drumhub.notification.domain.NotificationType;
import com.drumhub.notification.dto.CreateNotificationRequest;
import com.drumhub.notification.dto.NotificationResponse;
import com.drumhub.notification.dto.UnreadCountResponse;
import com.drumhub.notification.mapper.NotificationMapper;
import com.drumhub.notification.repository.NotificationRepository;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock private NotificationRepository notificationRepository;
    @Mock private UserRepository userRepository;
    @Mock private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationService notificationService;

    private static User buildUser(String username, Long id) {
        User user = User.builder()
                .username(username)
                .name("Test User")
                .email(username + "@test.com")
                .passwordHash("hash")
                .build();
        user.setId(id);
        return user;
    }

    private static NotificationResponse sampleResponse(Long id) {
        return new NotificationResponse(id, "follow", "drummer", false, null, null, null, Instant.now());
    }

    @Test
    void getNotifications_returnsPageForUser() {
        User recipient = buildUser("bernardp", 2L);
        Notification notification = Notification.builder()
                .recipient(recipient)
                .type(NotificationType.FOLLOW)
                .triggeredBy("drummer")
                .build();
        notification.setId(1L);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Notification> page = new PageImpl<>(List.of(notification), pageable, 1);

        when(userRepository.findByUsername("bernardp")).thenReturn(Optional.of(recipient));
        when(notificationRepository.findByRecipientIdAndActivoTrueOrderByCreatedAtDesc(2L, pageable))
                .thenReturn(page);
        when(notificationMapper.toResponse(notification)).thenReturn(sampleResponse(1L));

        Page<NotificationResponse> result = notificationService.getNotifications("bernardp", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).triggeredBy()).isEqualTo("drummer");
    }

    @Test
    void getUnreadCount_returnsCorrectCount() {
        User recipient = buildUser("bernardp", 2L);

        when(userRepository.findByUsername("bernardp")).thenReturn(Optional.of(recipient));
        when(notificationRepository.countByRecipientIdAndReadFalseAndActivoTrue(2L)).thenReturn(3L);

        UnreadCountResponse result = notificationService.getUnreadCount("bernardp");

        assertThat(result.unreadCount()).isEqualTo(3L);
    }

    @Test
    void markAllRead_callsRepository() {
        User recipient = buildUser("bernardp", 2L);

        when(userRepository.findByUsername("bernardp")).thenReturn(Optional.of(recipient));
        when(notificationRepository.markAllReadByRecipientId(2L)).thenReturn(3);

        notificationService.markAllRead("bernardp");

        verify(notificationRepository).markAllReadByRecipientId(2L);
    }

    @Test
    void push_whenRecipientNotFound_throwsResourceNotFoundException() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        CreateNotificationRequest request = new CreateNotificationRequest(
                "unknown", NotificationType.FOLLOW, "drummer", null, null, null);

        assertThatThrownBy(() -> notificationService.push(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("unknown");
    }

    @Test
    void push_whenValid_savesAndReturnsResponse() {
        User recipient = buildUser("bernardp", 2L);

        when(userRepository.findByUsername("bernardp")).thenReturn(Optional.of(recipient));

        Notification saved = Notification.builder()
                .recipient(recipient)
                .type(NotificationType.FOLLOW)
                .triggeredBy("drummer")
                .build();
        saved.setId(10L);

        when(notificationRepository.save(any(Notification.class))).thenReturn(saved);
        when(notificationMapper.toResponse(saved)).thenReturn(sampleResponse(10L));

        CreateNotificationRequest request = new CreateNotificationRequest(
                "bernardp", NotificationType.FOLLOW, "drummer", null, null, null);

        NotificationResponse result = notificationService.push(request);

        assertThat(result).isNotNull();
        assertThat(result.type()).isEqualTo("follow");
        verify(notificationRepository).save(any(Notification.class));
    }
}
