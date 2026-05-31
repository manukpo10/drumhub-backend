package com.drumhub.user.service;

import com.drumhub.common.exception.BadRequestException;
import com.drumhub.common.exception.ConflictException;
import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.notification.domain.NotificationType;
import com.drumhub.notification.dto.CreateNotificationRequest;
import com.drumhub.notification.service.NotificationService;
import com.drumhub.user.domain.Follow;
import com.drumhub.user.domain.User;
import com.drumhub.user.dto.FollowStatusResponse;
import com.drumhub.user.dto.UserSummaryResponse;
import com.drumhub.user.mapper.FollowMapper;
import com.drumhub.user.repository.FollowRepository;
import com.drumhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final FollowMapper followMapper;

    @Lazy
    @Autowired
    private NotificationService notificationService;

    @Transactional(readOnly = true)
    public Page<UserSummaryResponse> getFollowers(String username, Pageable pageable) {
        User user = findByUsername(username);
        return followRepository
                .findByFolloweeIdAndActivoTrue(user.getId(), pageable)
                .map(followMapper::followerToSummary);
    }

    @Transactional(readOnly = true)
    public Page<UserSummaryResponse> getFollowing(String username, Pageable pageable) {
        User user = findByUsername(username);
        return followRepository
                .findByFollowerIdAndActivoTrue(user.getId(), pageable)
                .map(followMapper::followeeToSummary);
    }

    @Transactional(readOnly = true)
    public FollowStatusResponse getStatus(String currentUsername, String targetUsername) {
        User current = findByUsername(currentUsername);
        User target = findByUsername(targetUsername);

        boolean following = followRepository
                .findByFollowerIdAndFolloweeId(current.getId(), target.getId())
                .map(Follow::isActivo)
                .orElse(false);
        long followerCount = followRepository.countByFolloweeIdAndActivoTrue(target.getId());

        return new FollowStatusResponse(targetUsername, following, followerCount);
    }

    @Transactional
    public FollowStatusResponse follow(String currentUsername, String targetUsername) {
        if (currentUsername.equals(targetUsername)) {
            throw new BadRequestException("Cannot follow yourself");
        }

        User follower = findByUsername(currentUsername);
        User followee = findByUsername(targetUsername);

        // Check for an existing soft-deleted record and reactivate it
        Follow follow = followRepository
                .findByFollowerIdAndFolloweeId(follower.getId(), followee.getId())
                .map(existing -> {
                    if (existing.isActivo()) {
                        throw new ConflictException("Already following");
                    }
                    existing.setActivo(true);
                    return existing;
                })
                .orElseGet(() -> Follow.builder()
                        .follower(follower)
                        .followee(followee)
                        .build());

        followRepository.save(follow);

        notificationService.push(new CreateNotificationRequest(
                followee.getUsername(),
                NotificationType.FOLLOW,
                follower.getUsername(),
                null, null, null
        ));

        long followerCount = followRepository.countByFolloweeIdAndActivoTrue(followee.getId());
        return new FollowStatusResponse(targetUsername, true, followerCount);
    }

    @Transactional
    public FollowStatusResponse unfollow(String currentUsername, String targetUsername) {
        User follower = findByUsername(currentUsername);
        User followee = findByUsername(targetUsername);

        Follow follow = followRepository
                .findByFollowerIdAndFolloweeId(follower.getId(), followee.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Not following this user"));

        follow.setActivo(false);
        followRepository.save(follow);

        long followerCount = followRepository.countByFolloweeIdAndActivoTrue(followee.getId());
        return new FollowStatusResponse(targetUsername, false, followerCount);
    }

    private User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}
