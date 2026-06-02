package com.drumhub.subscription.dto;

public record PricingTier(
        double monthly,
        double annual,
        double annualFullPrice,
        Integer monthlyArs,
        Integer annualArs,
        Integer annualFullArs
) {
    /** Convenience constructor — keeps existing call sites compiling with null ARS fields. */
    public PricingTier(double monthly, double annual, double annualFullPrice) {
        this(monthly, annual, annualFullPrice, null, null, null);
    }
}
