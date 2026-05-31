package com.drumhub.user.service;

import com.drumhub.common.exception.BadRequestException;
import com.drumhub.common.exception.ConflictException;
import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.notification.dto.CreateNotificationRequest;
import com.drumhub.notification.service.NotificationService;
import com.drumhub.user.domain.Follow;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.dto.FollowStatusResponse;
import com.drumhub.user.mapper.FollowMapper;
import com.drumhub.user.repository.FollowRepository;
import com.drumhub.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock private FollowRepository followRepository;
    @Mock private UserRepository userRepository;
    @Mock private FollowMapper followMapper;
    @Mock private NotificationService notificationService;

    @InjectMocks
    private FollowService followService;

    @BeforeEach
    void injectLazyDeps() {
        ReflectionTestUtils.setField(followService, "notificationService", notificationService);
    }

    private static User buildUser(String username, Long id) {
        User user = User.builder()
                .username(username)
                .name("Test User")
                .email(username + "@test.com")
                .passwordHash("hash")
                .plan(Plan.FREE)
                .build();
        user.setId(id);
        return user;
    }

    private static Follow activeFollow(User follower, User followee) {
        Follow f = Follow.builder().follower(follower).followee(followee).build();
        f.setActivo(true);
        return f;
    }

    @Test
    void follow_whenSelfFollow_throwsBadRequestException() {
        assertThatThrownBy(() -> followService.follow("drummer", "drummer"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Cannot follow yourself");
    }

    @Test
    void follow_whenAlreadyFollowing_throwsConflictException() {
        User follower = buildUser("drummer", 1L);
        User followee = buildUser("bernardp", 2L);
        Follow existing = activeFollow(follower, followee);

        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(follower));
        when(userRepository.findByUsername("bernardp")).thenReturn(Optional.of(followee));
        when(followRepository.findByFollowerIdAndFolloweeId(1L, 2L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> followService.follow("drummer", "bernardp"))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Already following");
    }

    @Test
    void follow_whenValid_savesAndReturnsFollowing() {
        User follower = buildUser("drummer", 1L);
        User followee = buildUser("bernardp", 2L);

        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(follower));
        when(userRepository.findByUsername("bernardp")).thenReturn(Optional.of(followee));
        when(followRepository.findByFollowerIdAndFolloweeId(1L, 2L)).thenReturn(Optional.empty());
        when(followRepository.save(any(Follow.class))).thenAnswer(inv -> inv.getArgument(0));
        when(followRepository.countByFolloweeIdAndActivoTrue(2L)).thenReturn(1L);
        when(notificationService.push(any(CreateNotificationRequest.class))).thenReturn(null);

        FollowStatusResponse result = followService.follow("drummer", "bernardp");

        assertThat(result.following()).isTrue();
        assertThat(result.followeeUsername()).isEqualTo("bernardp");
        assertThat(result.followerCount()).isEqualTo(1L);
        verify(followRepository).save(any(Follow.class));
    }

    @Test
    void unfollow_whenNotFollowing_throwsResourceNotFoundException() {
        User follower = buildUser("drummer", 1L);
        User followee = buildUser("bernardp", 2L);

        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(follower));
        when(userRepository.findByUsername("bernardp")).thenReturn(Optional.of(followee));
        when(followRepository.findByFollowerIdAndFolloweeId(1L, 2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> followService.unfollow("drummer", "bernardp"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Not following this user");
    }

    @Test
    void unfollow_whenValid_setsActivoFalseAndReturnsNotFollowing() {
        User follower = buildUser("drummer", 1L);
        User followee = buildUser("bernardp", 2L);
        Follow existing = activeFollow(follower, followee);

        when(userRepository.findByUsername("drummer")).thenReturn(Optional.of(follower));
        when(userRepository.findByUsername("bernardp")).thenReturn(Optional.of(followee));
        when(followRepository.findByFollowerIdAndFolloweeId(1L, 2L)).thenReturn(Optional.of(existing));
        when(followRepository.save(any(Follow.class))).thenAnswer(inv -> inv.getArgument(0));
        when(followRepository.countByFolloweeIdAndActivoTrue(2L)).thenReturn(0L);

        FollowStatusResponse result = followService.unfollow("drummer", "bernardp");

        assertThat(result.following()).isFalse();
        assertThat(result.followerCount()).isEqualTo(0L);
        assertThat(existing.isActivo()).isFalse();
        verify(followRepository).save(existing);
    }
}
