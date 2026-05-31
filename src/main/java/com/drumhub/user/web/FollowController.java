package com.drumhub.user.web;

import com.drumhub.common.web.ApiResponse;
import com.drumhub.user.dto.FollowStatusResponse;
import com.drumhub.user.dto.UserSummaryResponse;
import com.drumhub.user.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Follows", description = "Follow/unfollow users and browse follower lists")
public class FollowController {

    private final FollowService followService;

    @GetMapping("/{username}/followers")
    @Operation(summary = "List followers of a user")
    public ResponseEntity<ApiResponse<Page<UserSummaryResponse>>> getFollowers(
            @PathVariable String username,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(followService.getFollowers(username, pageable)));
    }

    @GetMapping("/{username}/following")
    @Operation(summary = "List users that a user follows")
    public ResponseEntity<ApiResponse<Page<UserSummaryResponse>>> getFollowing(
            @PathVariable String username,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(followService.getFollowing(username, pageable)));
    }

    @GetMapping("/{username}/follow/status")
    @Operation(summary = "Get follow status between current user and target user")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<FollowStatusResponse>> getStatus(
            @PathVariable String username,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                followService.getStatus(userDetails.getUsername(), username)
        ));
    }

    @PostMapping("/{username}/follow")
    @Operation(summary = "Follow a user")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<FollowStatusResponse>> follow(
            @PathVariable String username,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        FollowStatusResponse response = followService.follow(userDetails.getUsername(), username);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response, "Now following " + username));
    }

    @DeleteMapping("/{username}/follow")
    @Operation(summary = "Unfollow a user")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<FollowStatusResponse>> unfollow(
            @PathVariable String username,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                followService.unfollow(userDetails.getUsername(), username),
                "Unfollowed " + username
        ));
    }
}
