package com.drumhub.payment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.LongSupplier;

/**
 * Caches the MEP dollar rate with a 10-minute TTL.
 * Falls back to the last good cached rate, then to the configured fallback.
 *
 * <p>Time is injectable via a package-private constructor so tests can control the clock
 * without a Spring bean.
 */
@Slf4j
@Service
public class DollarRateService {

    static final long TTL_MS = 600_000L; // 10 minutes

    private final DollarRateProvider provider;
    private final double fallbackRate;
    private final LongSupplier clock;

    private Double cached;
    private long cachedAt;

    /** Production constructor — @Autowired tells Spring to use this one (two constructors exist). */
    @Autowired
    public DollarRateService(DollarRateProvider provider,
                              @Value("${app.pricing.fallback-mep}") double fallbackRate) {
        this(provider, fallbackRate, System::currentTimeMillis);
    }

    /** Package-private constructor for tests — allows injecting a fake clock. */
    DollarRateService(DollarRateProvider provider, double fallbackRate, LongSupplier clock) {
        this.provider     = provider;
        this.fallbackRate = fallbackRate;
        this.clock        = clock;
    }

    /**
     * Returns the current MEP rate.
     *
     * <ul>
     *   <li>If the cache is fresh (within TTL) → returns cached value immediately.</li>
     *   <li>If stale or absent → calls the provider:</li>
     *   <ul>
     *     <li>Success (rate &gt; 0) → stores and returns the new value.</li>
     *     <li>Failure + stale cache → returns the stale cached value.</li>
     *     <li>Failure + no cache → returns the configured fallback.</li>
     *   </ul>
     * </ul>
     */
    public double currentMep() {
        long now = clock.getAsLong();

        if (cached != null && (now - cachedAt) < TTL_MS) {
            return cached;
        }

        Optional<Double> fetched = provider.fetchMep();

        if (fetched.isPresent() && fetched.get() > 0) {
            cached   = fetched.get();
            cachedAt = now;
            return cached;
        }

        // Fetch failed or returned non-positive value
        if (cached != null) {
            log.warn("DollarRateService: fetch failed, returning stale cached rate {}", cached);
            return cached;
        }

        log.warn("DollarRateService: fetch failed and no cache, returning fallback rate {}", fallbackRate);
        return fallbackRate;
    }
}
