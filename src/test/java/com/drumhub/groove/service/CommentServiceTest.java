package com.drumhub.groove.service;

import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.groove.domain.Comment;
import com.drumhub.groove.domain.Groove;
import com.drumhub.groove.dto.CommentResponse;
import com.drumhub.groove.dto.CreateCommentRequest;
import com.drumhub.groove.mapper.CommentMapper;
import com.drumhub.groove.repository.CommentRepository;
import com.drumhub.groove.repository.GrooveRepository;
import com.drumhub.notification.domain.NotificationType;
import com.drumhub.notification.dto.CreateNotificationRequest;
import com.drumhub.notification.dto.NotificationResponse;
import com.drumhub.notification.service.NotificationService;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock private CommentRepository commentRepository;
    @Mock private GrooveRepository grooveRepository;
    @Mock private UserRepository userRepository;
    @Mock private CommentMapper commentMapper;
    @Mock private NotificationService notificationService;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void injectLazyDeps() {
        ReflectionTestUtils.setField(commentService, "notificationService", notificationService);
    }

    private static User buildUser(String username) {
        return User.builder()
                .username(username)
                .name("Test User")
                .email(username + "@test.com")
                .passwordHash("hash")
                .build();
    }

    private static Groove buildActiveGroove(Long id) {
        Groove groove = Groove.builder()
                .slug("test-groove")
                .title("Test Groove")
                .author(buildUser("author"))
                .bpm(120)
                .level("Básico")
                .build();
        groove.setId(id);
        groove.setActivo(true);
        return groove;
    }

    @Test
    void findByGrooveId_whenGrooveNotFound_throwsResourceNotFoundException() {
        when(grooveRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.findByGrooveId(99L, PageRequest.of(0, 10)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void addComment_whenValid_savesAndReturnsResponse() {
        Groove groove = buildActiveGroove(1L);
        User author = buildUser("commenter");

        when(grooveRepository.findById(1L)).thenReturn(Optional.of(groove));
        when(userRepository.findByUsername("commenter")).thenReturn(Optional.of(author));

        Comment savedComment = Comment.builder()
                .groove(groove)
                .author(author)
                .text("Great groove!")
                .build();

        CommentResponse expectedResponse = new CommentResponse(
                1L, "commenter", "Test User", "bonham", "Great groove!", Instant.now()
        );

        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);
        when(commentMapper.toResponse(savedComment)).thenReturn(expectedResponse);
        when(notificationService.push(any(CreateNotificationRequest.class))).thenReturn(null);

        CreateCommentRequest request = new CreateCommentRequest("Great groove!");
        CommentResponse result = commentService.addComment("commenter", 1L, request);

        assertThat(result.text()).isEqualTo("Great groove!");
        assertThat(result.authorUsername()).isEqualTo("commenter");
        verify(commentRepository).save(any(Comment.class));
    }
}
