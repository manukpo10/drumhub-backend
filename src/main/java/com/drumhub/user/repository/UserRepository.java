package com.drumhub.user.repository;

import com.drumhub.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<User> findByActivoTrue(Pageable pageable);

    @Query("""
            SELECT u FROM User u
            WHERE u.activo = true
            AND (
                LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(u.name) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            """)
    Page<User> searchByUsernameOrName(@Param("q") String q, Pageable pageable);

    /**
     * Bulk-downgrades all users whose paid plan or trial has expired to FREE.
     * Uses two separate conditions to handle COALESCE compatibility with H2 and PostgreSQL:
     *   1. Users with a non-null planExpiresAt that has passed
     *   2. Users with a non-null trialEndsAt (and no paid expiry) that has passed
     *
     * Both conditions share the same UPDATE — plan != 'FREE' guard ensures idempotency.
     */
    @Modifying
    @Query("""
            UPDATE User u
            SET u.plan = 'FREE', u.planExpiresAt = null, u.trialEndsAt = null
            WHERE u.plan <> 'FREE'
            AND (
                (u.planExpiresAt IS NOT NULL AND u.planExpiresAt < :now)
                OR (u.trialEndsAt IS NOT NULL AND u.trialEndsAt < :now AND u.planExpiresAt IS NULL)
            )
            """)
    int expireSubscriptions(@Param("now") Instant now);
}
