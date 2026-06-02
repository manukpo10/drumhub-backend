package com.drumhub.user.web;

import com.drumhub.common.web.ApiResponse;
import com.drumhub.user.dto.AuthResponse;
import com.drumhub.user.dto.GoogleAuthRequest;
import com.drumhub.user.dto.LoginRequest;
import com.drumhub.user.dto.RegisterRequest;
import com.drumhub.user.dto.UserResponse;
import com.drumhub.user.service.GoogleAuthService;
import com.drumhub.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Registration and login")
public class AuthController {

    private final UserService userService;
    private final GoogleAuthService googleAuthService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        UserResponse user = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(user, "User registered successfully"));
    }

    @PostMapping("/login")
    @Operation(summary = "Login and receive a JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse auth = userService.login(request);
        return ResponseEntity.ok(ApiResponse.ok(auth, "Login successful"));
    }

    @PostMapping("/google")
    @Operation(summary = "Sign in or register with a Google ID token")
    public ResponseEntity<ApiResponse<AuthResponse>> googleSignIn(
            @RequestBody GoogleAuthRequest request
    ) {
        AuthResponse auth = googleAuthService.signIn(request.credential());
        return ResponseEntity.ok(ApiResponse.ok(auth, "Google sign-in successful"));
    }
}
