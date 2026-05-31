package com.drumhub.groove.web;

import com.drumhub.common.web.ApiResponse;
import com.drumhub.groove.dto.FavoriteResponse;
import com.drumhub.groove.dto.FavoriteStatusResponse;
import com.drumhub.groove.service.FavoriteService;
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
@RequestMapping("/api/users/me/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorites", description = "User favorites management")
@SecurityRequirement(name = "bearerAuth")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FavoriteResponse>>> getFavorites(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                favoriteService.getFavorites(userDetails.getUsername(), pageable)
        ));
    }

    @GetMapping("/{grooveId}/status")
    public ResponseEntity<ApiResponse<FavoriteStatusResponse>> getStatus(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long grooveId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                favoriteService.getStatus(userDetails.getUsername(), grooveId)
        ));
    }

    @PostMapping("/{grooveId}")
    public ResponseEntity<ApiResponse<FavoriteStatusResponse>> addFavorite(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long grooveId
    ) {
        FavoriteStatusResponse response = favoriteService.addFavorite(userDetails.getUsername(), grooveId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response, "Added to favorites"));
    }

    @DeleteMapping("/{grooveId}")
    public ResponseEntity<ApiResponse<FavoriteStatusResponse>> removeFavorite(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long grooveId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                favoriteService.removeFavorite(userDetails.getUsername(), grooveId),
                "Removed from favorites"
        ));
    }
}
