package com.drumhub.user.service;

import com.drumhub.common.exception.UnauthorizedException;
import com.drumhub.security.JwtService;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.dto.AuthResponse;
import com.drumhub.user.mapper.UserMapper;
import com.drumhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private static final String TOKENINFO_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";
    private static final String[] DEFAULT_COLORS = {"#e8ff00", "#ff6b35", "#a855f7", "#06b6d4", "#10b981"};

    @Value("${google.client-id}")
    private String googleClientId;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final RestClient restClient = RestClient.create();

    @Transactional
    public AuthResponse signIn(String credential) {
        Map<?, ?> payload = verifyToken(credential);

        String email = String.valueOf(payload.get("email"));
        String googleName = payload.containsKey("name") ? String.valueOf(payload.get("name")) : null;
        if (googleName == null || googleName.isBlank() || "null".equals(googleName)) {
            googleName = email.split("@")[0];
        }

        final String resolvedName = googleName;
        Optional<User> existing = userRepository.findByEmail(email);
        User user = existing.orElseGet(() -> createGoogleUser(email, resolvedName));

        String token = jwtService.generateToken(user);
        return AuthResponse.of(token, userMapper.toResponse(user));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> verifyToken(String credential) {
        try {
            Map<String, Object> payload = restClient.get()
                    .uri(TOKENINFO_URL + credential)
                    .retrieve()
                    .body(Map.class);

            if (payload == null) {
                throw new UnauthorizedException("Empty response from Google tokeninfo");
            }

            String aud = String.valueOf(payload.get("aud"));
            if (!googleClientId.equals(aud)) {
                log.warn("Google token aud mismatch: expected={} got={}", googleClientId, aud);
                throw new UnauthorizedException("Invalid Google token audience");
            }

            String emailVerified = String.valueOf(payload.get("email_verified"));
            if (!"true".equals(emailVerified)) {
                throw new UnauthorizedException("Google email not verified");
            }

            return payload;
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Google token verification failed: {}", e.getMessage());
            throw new UnauthorizedException("Invalid Google token");
        }
    }

    private User createGoogleUser(String email, String googleName) {
        // Derive username from email prefix, sanitize, ensure uniqueness
        String base = email.split("@")[0].replaceAll("[^a-zA-Z0-9_]", "_");
        if (base.length() > 20) base = base.substring(0, 20);
        if (base.isEmpty()) base = "drummer";

        String username = base;
        int suffix = 1;
        while (userRepository.existsByUsername(username)) {
            username = base + suffix++;
        }

        String color = DEFAULT_COLORS[Math.abs(username.hashCode()) % DEFAULT_COLORS.length];
        String init = String.valueOf(Character.toUpperCase(googleName.charAt(0)));

        User user = User.builder()
                .username(username)
                .name(googleName)
                .email(email)
                // Google users have no password — store a random unguessable hash
                .passwordHash(passwordEncoder.encode(UUID.randomUUID().toString()))
                .avatarSeed("bonham")
                .color(color)
                .init(init)
                .plan(Plan.FREE)
                .build();

        log.info("Created new user via Google Sign-In: username={} email={}", username, email);
        return userRepository.save(user);
    }
}
