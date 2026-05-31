package com.drumhub.groove.repository;

import com.drumhub.groove.domain.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserIdAndGrooveId(Long userId, Long grooveId);

    boolean existsByUserIdAndGrooveId(Long userId, Long grooveId);

    Page<Favorite> findByUserIdAndActivoTrueOrderByCreatedAtDesc(Long userId, Pageable pageable);

    long countByGrooveIdAndActivoTrue(Long grooveId);

    long countByUserUsernameAndActivoTrue(String username);
}
