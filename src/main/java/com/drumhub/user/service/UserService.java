package com.drumhub.user.service;

import com.drumhub.common.exception.ConflictException;
import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.common.exception.UnauthorizedException;
import com.drumhub.security.JwtService;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.dto.AuthResponse;
import com.drumhub.user.dto.LoginRequest;
import com.drumhub.user.dto.RegisterRequest;
import com.drumhub.user.dto.UpdateAvatarRequest;
import com.drumhub.user.dto.UpdateEmailRequest;
import com.drumhub.user.dto.UpdatePasswordRequest;
import com.drumhub.user.dto.UpdateProfileRequest;
import com.drumhub.user.dto.UserResponse;
import com.drumhub.user.mapper.UserMapper;
import com.drumhub.user.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private static final String[] DEFAULT_COLORS = {
            "#e8ff00", "#ff6b35", "#a855f7", "#06b6d4", "#10b981"
    };

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // @Lazy on AuthenticationManager breaks the circular dependency:
    // SecurityConfig -> JwtAuthFilter -> UserService -> AuthenticationManager -> SecurityConfig
    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Lazy AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Transactional
    public UserResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new ConflictException("Username already taken: " + req.username());
        }
        if (userRepository.existsByEmail(req.email())) {
            throw new ConflictException("Email already registered: " + req.email());
        }

        String avatarSeed = req.avatarSeed() != null && !req.avatarSeed().isBlank()
                ? req.avatarSeed()
                : "bonham";

        String color = selectColor(req.username());

        String init = req.name() != null && !req.name().isBlank()
                ? String.valueOf(Character.toUpperCase(req.name().charAt(0)))
                : null;

        User user = User.builder()
                .username(req.username())
                .name(req.name())
                .email(req.email())
                .passwordHash(passwordEncoder.encode(req.password()))
                .avatarSeed(avatarSeed)
                .color(color)
                .init(init)
                .plan(Plan.FREE)
                .build();

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Transactional
    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        User user = userRepository.findByUsername(req.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + req.username()));

        String token = jwtService.generateToken(user);
        return AuthResponse.of(token, userMapper.toResponse(user));
    }

    @Transactional(readOnly = true)
    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .filter(User::isActivo)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(String q, String sort, Pageable pageable) {
        Sort resolvedSort = resolveSort(sort);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), resolvedSort);

        Page<User> page;
        if (q != null && !q.isBlank()) {
            page = userRepository.searchByUsernameOrName(q.trim(), sortedPageable);
        } else {
            page = userRepository.findByActivoTrue(sortedPageable);
        }
        return page.map(userMapper::toResponse);
    }

    @Transactional
    public UserResponse updateProfile(String currentUsername, UpdateProfileRequest req) {
        User user = loadActiveUser(currentUsername);
        user.setName(req.name());
        user.setBio(req.bio());
        // Refresh init when name changes
        if (req.name() != null && !req.name().isBlank()) {
            user.setInit(String.valueOf(Character.toUpperCase(req.name().charAt(0))));
        }
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateEmail(String currentUsername, UpdateEmailRequest req) {
        User user = loadActiveUser(currentUsername);
        verifyPassword(req.currentPassword(), user.getPasswordHash());

        if (userRepository.existsByEmail(req.email())) {
            throw new ConflictException("Email already in use: " + req.email());
        }
        user.setEmail(req.email());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void updatePassword(String currentUsername, UpdatePasswordRequest req) {
        User user = loadActiveUser(currentUsername);
        verifyPassword(req.currentPassword(), user.getPasswordHash());
        user.setPasswordHash(passwordEncoder.encode(req.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public UserResponse updateAvatar(String currentUsername, UpdateAvatarRequest req) {
        User user = loadActiveUser(currentUsername);
        user.setAvatarSeed(req.avatarSeed());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String username) {
        return findByUsername(username);
    }

    // -- Private helpers --

    private User loadActiveUser(String username) {
        return userRepository.findByUsername(username)
                .filter(User::isActivo)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private void verifyPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new UnauthorizedException("Invalid current password");
        }
    }

    private Sort resolveSort(String sort) {
        if (sort == null) {
            return Sort.by(Sort.Direction.ASC, "username");
        }
        return switch (sort) {
            case "name" -> Sort.by(Sort.Direction.ASC, "name");
            // grooves and likes will be implemented in the groove slice
            default -> Sort.by(Sort.Direction.ASC, "username");
        };
    }

    private String selectColor(String username) {
        int idx = Math.abs(username.hashCode()) % DEFAULT_COLORS.length;
        return DEFAULT_COLORS[idx];
    }
}
