package com.drumhub.subscription.service;

import com.drumhub.common.exception.BadRequestException;
import com.drumhub.common.exception.ConflictException;
import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.payment.config.PlanPrices;
import com.drumhub.payment.service.DollarRateService;
import com.drumhub.subscription.dto.CurrentPlanResponse;
import com.drumhub.subscription.dto.PlanFeatures;
import com.drumhub.subscription.dto.PlanResponse;
import com.drumhub.subscription.dto.PricingTier;
import com.drumhub.user.domain.Plan;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
    private final DollarRateService dollarRateService;

    @Transactional(readOnly = true)
    public List<PlanResponse> getPlans() {
        double mep = dollarRateService.currentMep();
        return PLAN_CATALOG.stream()
                .map(plan -> {
                    PricingTier tier = plan.pricing();
                    int monthlyArs    = PlanPrices.toArs(tier.monthly(),         mep);
                    int annualArs     = PlanPrices.toArs(tier.annual(),          mep);
                    int annualFullArs = PlanPrices.toArs(tier.annualFullPrice(), mep);
                    PricingTier enriched = new PricingTier(
                            tier.monthly(), tier.annual(), tier.annualFullPrice(),
                            monthlyArs, annualArs, annualFullArs
                    );
                    return new PlanResponse(plan.id(), plan.name(), enriched, plan.features());
                })
                .toList();
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

    // Only downgrade to free is allowed via this endpoint.
    // Upgrades to pro/studio happen exclusively through the Mercado Pago payment webhook
    // (server-to-server, signature-validated) to prevent self-service plan manipulation.
    @Transactional
    public CurrentPlanResponse downgradePlan(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        user.setPlan(Plan.FREE);
        userRepository.save(user);

        PlanResponse freePlan = PLAN_CATALOG.stream()
                .filter(p -> p.id().equals("free"))
                .findFirst()
                .orElseThrow();

        return new CurrentPlanResponse("free", username, freePlan.features());
    }

    // Called only from the Mercado Pago webhook after payment is confirmed server-side.
    // 2-arg overload delegates to 4-arg with null expiry and payment ID (manual/admin activations).
    @Transactional
    public CurrentPlanResponse activatePaidPlan(String username, String planId) {
        return activatePaidPlan(username, planId, null, null);
    }

    @Transactional
    public CurrentPlanResponse activatePaidPlan(String username, String planId, Instant planExpiresAt, String mpPaymentId) {
        String normalizedPlan = planId.toLowerCase();
        if (!Set.of("pro", "studio").contains(normalizedPlan)) {
            throw new BadRequestException("Invalid paid plan: " + planId);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Plan newPlan = Plan.valueOf(normalizedPlan.toUpperCase());
        user.setPlan(newPlan);
        user.setPlanExpiresAt(planExpiresAt);
        user.setMpPaymentId(mpPaymentId);
        user.setTrialEndsAt(null); // clear trial when paid plan activates

        userRepository.save(user);

        PlanResponse matchingPlan = PLAN_CATALOG.stream()
                .filter(p -> p.id().equals(normalizedPlan))
                .findFirst()
                .orElseThrow();

        return new CurrentPlanResponse(normalizedPlan, username, matchingPlan.features());
    }

    @Transactional
    public CurrentPlanResponse startTrial(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Instant now = Instant.now();

        boolean hasActivePaidPlan = user.getPlanExpiresAt() != null && user.getPlanExpiresAt().isAfter(now);
        boolean hasActiveTrial = user.getTrialEndsAt() != null && user.getTrialEndsAt().isAfter(now);

        if (hasActivePaidPlan || hasActiveTrial) {
            throw new ConflictException("User already has an active plan or trial.");
        }

        user.setPlan(Plan.PRO);
        user.setTrialEndsAt(now.plus(7, ChronoUnit.DAYS));
        userRepository.save(user);

        PlanResponse proPlan = PLAN_CATALOG.stream()
                .filter(p -> p.id().equals("pro"))
                .findFirst()
                .orElseThrow();

        return new CurrentPlanResponse("pro", username, proPlan.features());
    }

    @Transactional
    public int expireOverduePlans() {
        return userRepository.expireSubscriptions(Instant.now());
    }
}
