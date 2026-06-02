package com.drumhub.payment.service;

import java.util.Optional;

/**
 * Strategy interface for fetching the MEP (bolsa) dollar rate.
 * Returns the "venta" rate, or empty on any failure.
 */
public interface DollarRateProvider {

    Optional<Double> fetchMep();
}
