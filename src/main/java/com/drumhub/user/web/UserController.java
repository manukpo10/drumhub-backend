package com.drumhub.user.web;

import com.drumhub.common.web.ApiResponse;
import com.drumhub.user.dto.UpdateAvatarRequest;
import com.drumhub.user.dto.UpdateEmailRequest;
import com.drumhub.user.dto.UpdatePasswordRequest;
import com.drumhub.user.dto.UpdateProfileRequest;
import com.drumhub.user.dto.UserResponse;
import com.drumhub.user.service.SupabaseStorageService;
import com.drumhub.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User profiles and account management")
public class UserController {

    private final UserService userService;
    private final SupabaseStorageService supabaseStorageService;

    @GetMapping
    @Operation(summary = "List all users (pageable, filterable)")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> listUsers(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String sort,
            @ParameterObject Pageable pageable
    ) {
        Page<UserResponse> page = userService.findAll(q, sort, pageable);
        return ResponseEntity.ok(ApiResponse.ok(page));
    }

    // NOTE: /me must be mapped BEFORE /{username} so Spring's literal path takes precedence
    @GetMapping("/me")
    @Operation(summary = "Get own profile", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<UserResponse>> getMe(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserResponse user = userService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(user));
    }

    @PutMapping("/me")
    @Operation(summary = "Update own profile", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        UserResponse user = userService.updateProfile(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.ok(user, "Profile updated"));
    }

    @PutMapping("/me/email")
    @Operation(summary = "Update own email", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<UserResponse>> updateEmail(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateEmailRequest request
    ) {
        UserResponse user = userService.updateEmail(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.ok(user, "Email updated"));
    }

    @PutMapping("/me/password")
    @Operation(summary = "Update own password", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> updatePassword(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdatePasswordRequest request
    ) {
        userService.updatePassword(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/me/avatar")
    @Operation(summary = "Update own avatar seed", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<UserResponse>> updateAvatar(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateAvatarRequest request
    ) {
        UserResponse user = userService.updateAvatar(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.ok(user, "Avatar updated"));
    }

    @PostMapping("/me/avatar-photo")
    @Operation(summary = "Upload own avatar photo", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<UserResponse>> uploadAvatarPhoto(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String avatarUrl = supabaseStorageService.uploadAvatar(
                    userDetails.getUsername(),
                    file.getBytes(),
                    file.getContentType()
            );
            UserResponse user = userService.updateAvatarPhoto(userDetails.getUsername(), avatarUrl);
            return ResponseEntity.ok(ApiResponse.ok(user, "Avatar photo uploaded"));
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to read uploaded file", e);
        }
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get public profile by username")
    public ResponseEntity<ApiResponse<UserResponse>> getByUsername(
            @PathVariable String username
    ) {
        UserResponse user = userService.findByUsername(username);
        return ResponseEntity.ok(ApiResponse.ok(user));
    }
}
