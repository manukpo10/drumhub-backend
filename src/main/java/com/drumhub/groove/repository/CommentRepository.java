package com.drumhub.groove.repository;

import com.drumhub.groove.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByGrooveIdAndActivoTrueOrderByCreatedAtDesc(Long grooveId, Pageable pageable);

    long countByGrooveIdAndActivoTrue(Long grooveId);

    /** Active comment count per groove id, for list facet counts. Returns rows of [grooveId, count]. */
    @Query("SELECT c.groove.id, COUNT(c) FROM Comment c WHERE c.activo = true AND c.groove.id IN :ids GROUP BY c.groove.id")
    List<Object[]> countActiveByGrooveIds(@Param("ids") List<Long> ids);

    /** Recent comments for the activity feed — author and groove eagerly fetched. */
    @Query("SELECT c FROM Comment c JOIN FETCH c.author JOIN FETCH c.groove WHERE c.activo = true ORDER BY c.createdAt DESC")
    List<Comment> findRecentForActivityFeed(Pageable pageable);

    /** Recent comments by a specific user for the profile activity feed. */
    @Query("SELECT c FROM Comment c JOIN FETCH c.author JOIN FETCH c.groove WHERE c.activo = true AND c.author.username = :username ORDER BY c.createdAt DESC")
    List<Comment> findRecentByUserForActivityFeed(@Param("username") String username, Pageable pageable);
}
