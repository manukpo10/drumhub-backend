package com.drumhub.subscription.service;

import com.drumhub.common.exception.BadRequestException;
import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.subscription.dto.CurrentPlanResponse;
import com.drumhub.subscription.dto.PlanFeatures;
import com.drumhub.subscription.dto.PlanResponse;
import com.drumhub.subscription.dto.PricingTier;
import com.drumhub.subscription.dto.UpdatePlanRequest;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private static final Set<String> VALID_PLAN_IDS = Set.of("free", "pro", "studio");

    private static final List<PlanResponse> PLAN_CATALOG = List.of(
            new PlanResponse("free", "Free",
                    new PricingTier(0, 0, 0),
                    new PlanFeatures("20", "5", true, false, false, "none", false, false, "no", false)),
            new PlanResponse("pro", "Pro",
                    new PricingTier(5, 48, 60),
                    new PlanFeatures("unlimited", "unlimited", true, true, true, "PRO", false, false, "no", false)),
            new PlanResponse("studio", "Studio",
                    new PricingTier(12, 116, 144),
                    new PlanFeatures("unlimited", "unlimited", true, true, true, "STUDIO", true, true, "up to 5", true))
    );

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<PlanResponse> getPlans() {
        return PLAN_CATALOG;
    }

    @Transactional(readOnly = true)
    public CurrentPlanResponse getCurrentPlan(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        String planId = user.getPlan().name().toLowerCase();
        PlanResponse matchingPlan = PLAN_CATALOG.stream()
                .filter(p -> p.id().equals(planId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + planId));

        return new CurrentPlanResponse(planId, username, matchingPlan.features());
    }

    @Transactional
    public CurrentPlanResponse updatePlan(String username, UpdatePlanRequest request) {
        String planId = request.plan().toLowerCase();
        if (!VALID_PLAN_IDS.contains(planId)) {
            throw new BadRequestException("Invalid plan. Must be one of: free, pro, studio");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Plan newPlan = Plan.valueOf(planId.toUpperCase());
        user.setPlan(newPlan);
        userRepository.save(user);

        PlanResponse matchingPlan = PLAN_CATALOG.stream()
                .filter(p -> p.id().equals(planId))
                .findFirst()
                .orElseThrow();

        return new CurrentPlanResponse(planId, username, matchingPlan.features());
    }
}
