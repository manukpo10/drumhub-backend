package com.drumhub.user.domain;

import com.drumhub.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true, nullable = false, length = 50, updatable = false)
    private String username;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(length = 200)
    private String bio;

    @Column(length = 50, columnDefinition = "varchar(50) default 'bonham'")
    private String avatarSeed;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(length = 7)
    private String color;

    @Column(length = 1)
    private String init;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Plan plan;

    @Column(name = "plan_expires_at")
    private Instant planExpiresAt;

    @Column(name = "trial_ends_at")
    private Instant trialEndsAt;

    @Column(name = "mp_payment_id", length = 50)
    private String mpPaymentId;

    // UserDetails interface implementation

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActivo();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActivo();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActivo();
    }

    @Override
    public boolean isEnabled() {
        return isActivo();
    }

    public String getDisplayName() {
        return name;
    }
}
