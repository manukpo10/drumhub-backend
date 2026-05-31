package com.drumhub.subscription.dto;

public record CurrentPlanResponse(
        String plan,
        String username,
        PlanFeatures features
) {}
