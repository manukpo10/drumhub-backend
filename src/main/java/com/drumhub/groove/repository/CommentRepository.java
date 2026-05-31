package com.drumhub.groove.repository;

import com.drumhub.groove.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByGrooveIdAndActivoTrueOrderByCreatedAtDesc(Long grooveId, Pageable pageable);

    long countByGrooveIdAndActivoTrue(Long grooveId);
}
