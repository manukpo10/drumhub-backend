package com.drumhub.activity.service;

import com.drumhub.activity.dto.ActivityEventDto;
import com.drumhub.groove.repository.CommentRepository;
import com.drumhub.groove.repository.FavoriteRepository;
import com.drumhub.groove.repository.GrooveRepository;
import com.drumhub.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    /** Events fetched per source before merging. Fetching more than needed so the
     *  final merged/sorted slice of {@code size} contains the truly most recent ones. */
    private static final int FETCH_PER_SOURCE = 30;

    private final GrooveRepository grooveRepository;
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;
    private final FollowRepository followRepository;

    /**
     * Returns the {@code size} most recent community events, merging uploads,
     * comments, likes and follows sorted by createdAt descending.
     */
    @Transactional(readOnly = true)
    public List<ActivityEventDto> getRecentFeed(int size) {
        var pageable = PageRequest.of(0, FETCH_PER_SOURCE);
        List<ActivityEventDto> events = new ArrayList<>();

        // ── Uploads ──
        grooveRepository.findRecentForActivityFeed(pageable).forEach(g ->
                events.add(new ActivityEventDto(
                        "upload",
                        g.getAuthor().getUsername(),
                        g.getAuthor().getName(),
                        g.getTitle(), g.getSlug(),
                        null, null,
                        g.getCreatedAt()
                ))
        );

        // ── Comments ──
        commentRepository.findRecentForActivityFeed(pageable).forEach(c ->
                events.add(new ActivityEventDto(
                        "comment",
                        c.getAuthor().getUsername(),
                        c.getAuthor().getName(),
                        c.getGroove().getTitle(), c.getGroove().getSlug(),
                        null, null,
                        c.getCreatedAt()
                ))
        );

        // ── Likes (favorites) ──
        favoriteRepository.findRecentForActivityFeed(pageable).forEach(f ->
                events.add(new ActivityEventDto(
                        "like",
                        f.getUser().getUsername(),
                        f.getUser().getName(),
                        f.getGroove().getTitle(), f.getGroove().getSlug(),
                        null, null,
                        f.getCreatedAt()
                ))
        );

        // ── Follows ──
        followRepository.findRecentForActivityFeed(pageable).forEach(f ->
                events.add(new ActivityEventDto(
                        "follow",
                        f.getFollower().getUsername(),
                        f.getFollower().getName(),
                        null, null,
                        f.getFollowee().getUsername(),
                        f.getFollowee().getName(),
                        f.getCreatedAt()
                ))
        );

        // Merge, sort newest-first, take top N
        return events.stream()
                .sorted(Comparator.comparing(ActivityEventDto::createdAt).reversed())
                .limit(size)
                .toList();
    }
}
