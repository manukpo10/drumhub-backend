package com.drumhub.groove.web;

import com.drumhub.common.web.ApiResponse;
import com.drumhub.groove.dto.CommentResponse;
import com.drumhub.groove.dto.CreateCommentRequest;
import com.drumhub.groove.service.CommentService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grooves/{grooveId}/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Groove comment management")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getComments(
            @PathVariable Long grooveId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(commentService.findByGrooveId(grooveId, pageable)));
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable Long grooveId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        CommentResponse response = commentService.addComment(userDetails.getUsername(), grooveId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response, "Comment added"));
    }
}
