package com.drumhub.groove.repository;

import com.drumhub.groove.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByGrooveIdAndActivoTrueOrderByCreatedAtDesc(Long grooveId, Pageable pageable);

    long countByGrooveIdAndActivoTrue(Long grooveId);

    /** Recent comments for the activity feed — author and groove eagerly fetched. */
    @Query("SELECT c FROM Comment c JOIN FETCH c.author JOIN FETCH c.groove WHERE c.activo = true ORDER BY c.createdAt DESC")
    List<Comment> findRecentForActivityFeed(Pageable pageable);
}
