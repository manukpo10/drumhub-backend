package com.drumhub.groove.repository;

import com.drumhub.groove.domain.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserIdAndGrooveId(Long userId, Long grooveId);

    boolean existsByUserIdAndGrooveId(Long userId, Long grooveId);

    boolean existsByUserIdAndGrooveIdAndActivoTrue(Long userId, Long grooveId);

    Page<Favorite> findByUserIdAndActivoTrueOrderByCreatedAtDesc(Long userId, Pageable pageable);

    long countByGrooveIdAndActivoTrue(Long grooveId);

    long countByUserUsernameAndActivoTrue(String username);

    /** Recent likes for the activity feed — user and groove eagerly fetched. */
    @Query("SELECT f FROM Favorite f JOIN FETCH f.user JOIN FETCH f.groove WHERE f.activo = true ORDER BY f.createdAt DESC")
    List<Favorite> findRecentForActivityFeed(Pageable pageable);
}
