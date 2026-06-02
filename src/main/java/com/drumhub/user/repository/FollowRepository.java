package com.drumhub.user.repository;

import com.drumhub.user.domain.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    /** Followers of a user: follow records where followee = user */
    Page<Follow> findByFolloweeIdAndActivoTrue(Long followeeId, Pageable pageable);

    /** Users someone follows: follow records where follower = user */
    Page<Follow> findByFollowerIdAndActivoTrue(Long followerId, Pageable pageable);

    long countByFolloweeIdAndActivoTrue(Long followeeId);

    long countByFollowerIdAndActivoTrue(Long followerId);

    /** Recent follows for the activity feed — follower and followee eagerly fetched. */
    @Query("SELECT f FROM Follow f JOIN FETCH f.follower JOIN FETCH f.followee WHERE f.activo = true ORDER BY f.createdAt DESC")
    List<Follow> findRecentForActivityFeed(Pageable pageable);
}
