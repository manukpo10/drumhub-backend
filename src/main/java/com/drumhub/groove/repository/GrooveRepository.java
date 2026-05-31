package com.drumhub.groove.repository;

import com.drumhub.groove.domain.Groove;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GrooveRepository extends JpaRepository<Groove, Long>, JpaSpecificationExecutor<Groove> {

    Optional<Groove> findBySlugAndActivoTrue(String slug);

    Optional<Groove> findFirstByFeaturedTrueAndActivoTrue();

    boolean existsBySlug(String slug);

    @Query("SELECT g FROM Groove g WHERE g.activo = true ORDER BY (g.plays + g.likes * 2) DESC")
    Page<Groove> findTrending(Pageable pageable);

    long countByAuthorUsernameAndActivoTrue(String username);
}
