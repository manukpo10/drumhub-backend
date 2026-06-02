package com.drumhub.payment.config;

import java.util.Map;

/**
 * Single source of truth for plan prices in USD.
 * ARS conversion uses the same rounding formula as the frontend:
 * Math.round(usd * mep / 100) * 100
 */
public final class PlanPrices {

    public record Usd(int monthly, int annual, int annualFull) {}

    public static final Usd FREE   = new Usd(0,  0,   0);
    public static final Usd PRO    = new Usd(5,  48,  60);
    public static final Usd STUDIO = new Usd(12, 116, 144);

    /** Only paid plans — used by CheckoutService. */
    public static final Map<String, Usd> PAID = Map.of(
            "pro",    PRO,
            "studio", STUDIO
    );

    /**
     * Converts a USD amount to ARS, rounded to the nearest 100.
     * Matches the frontend formula: {@code Math.round(usd * mep / 100) * 100}.
     */
    public static int toArs(double usd, double mep) {
        return (int) (Math.round(usd * mep / 100.0) * 100);
    }

    private PlanPrices() {}
}
