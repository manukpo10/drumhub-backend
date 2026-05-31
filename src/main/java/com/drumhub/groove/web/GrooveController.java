package com.drumhub.groove.web;

import com.drumhub.common.web.ApiResponse;
import com.drumhub.groove.dto.CreateGrooveRequest;
import com.drumhub.groove.dto.GrooveResponse;
import com.drumhub.groove.dto.UpdateGrooveRequest;
import com.drumhub.groove.service.GrooveService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grooves")
@RequiredArgsConstructor
@Tag(name = "Grooves", description = "Groove sharing and management")
public class GrooveController {

    private final GrooveService grooveService;

    @GetMapping
    @Operation(summary = "List grooves with optional filters and pagination")
    public ResponseEntity<ApiResponse<Page<GrooveResponse>>> getAll(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) Integer bpmMin,
            @RequestParam(required = false) Integer bpmMax,
            @RequestParam(required = false) String sort,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                grooveService.findAll(q, genre, level, tag, bpmMin, bpmMax, sort, pageable)
        ));
    }

    // NOTE: /featured must come BEFORE /{idOrSlug} to avoid "featured" being treated as a slug
    @GetMapping("/featured")
    @Operation(summary = "Get the current featured groove")
    public ResponseEntity<ApiResponse<GrooveResponse>> getFeatured() {
        return ResponseEntity.ok(ApiResponse.ok(grooveService.findFeatured()));
    }

    @GetMapping("/{idOrSlug}")
    @Operation(summary = "Get a groove by numeric ID or slug")
    public ResponseEntity<ApiResponse<GrooveResponse>> getByIdOrSlug(@PathVariable String idOrSlug) {
        return ResponseEntity.ok(ApiResponse.ok(grooveService.findByIdOrSlug(idOrSlug)));
    }

    @PostMapping
    @Operation(summary = "Create a new groove", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<GrooveResponse>> create(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateGrooveRequest request
    ) {
        GrooveResponse response = grooveService.create(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response, "Groove created"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing groove", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<GrooveResponse>> update(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateGrooveRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(grooveService.update(userDetails.getUsername(), id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a groove", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> delete(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        grooveService.delete(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/plays")
    @Operation(summary = "Increment play count for a groove")
    public ResponseEntity<ApiResponse<GrooveResponse>> incrementPlays(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(grooveService.incrementPlays(id)));
    }
}
