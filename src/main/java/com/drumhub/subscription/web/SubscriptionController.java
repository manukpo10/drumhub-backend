package com.drumhub.subscription.web;

import com.drumhub.common.web.ApiResponse;
import com.drumhub.subscription.dto.CurrentPlanResponse;
import com.drumhub.subscription.dto.PlanResponse;
import com.drumhub.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Subscription & Pricing", description = "Plan catalog and user plan management")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/api/pricing/plans")
    @Operation(summary = "List all available pricing plans")
    public ResponseEntity<ApiResponse<List<PlanResponse>>> getPlans() {
        return ResponseEntity.ok(ApiResponse.ok(subscriptionService.getPlans()));
    }

    @GetMapping("/api/users/me/plan")
    @Operation(summary = "Get the authenticated user's current plan")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<CurrentPlanResponse>> getCurrentPlan(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                subscriptionService.getCurrentPlan(userDetails.getUsername())
        ));
    }

    // Downgrades the user to the free plan.
    // Upgrades happen exclusively via the Mercado Pago webhook — never from the client.
    @DeleteMapping("/api/users/me/plan")
    @Operation(summary = "Cancel subscription and revert to free plan")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<CurrentPlanResponse>> cancelPlan(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                subscriptionService.downgradePlan(userDetails.getUsername())
        ));
    }

    @PostMapping("/api/users/me/trial")
    @Operation(summary = "Activate a 7-day PRO trial for the authenticated user")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<CurrentPlanResponse>> activateTrial(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                subscriptionService.startTrial(userDetails.getUsername())
        ));
    }
}
