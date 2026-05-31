package com.drumhub.genre.repository;

import com.drumhub.genre.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findBySlug(String slug);

    Optional<Genre> findByName(String name);

    List<Genre> findByActivoTrueOrderByNameAsc();

    boolean existsBySlug(String slug);
}
