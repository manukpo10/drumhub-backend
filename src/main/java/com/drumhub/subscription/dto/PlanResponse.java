package com.drumhub.subscription.dto;

public record PlanResponse(
        String id,
        String name,
        PricingTier pricing,
        PlanFeatures features
) {}
