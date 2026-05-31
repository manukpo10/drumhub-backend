package com.drumhub.user.repository;

import com.drumhub.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
