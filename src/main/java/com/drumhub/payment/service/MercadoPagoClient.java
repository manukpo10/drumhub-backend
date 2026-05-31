package com.drumhub.payment.service;

import java.util.Map;

/**
 * Thin outbound HTTP client for Mercado Pago API calls.
 * Backed by Java 21 HttpClient in production; mockable in tests.
 */
public interface MercadoPagoClient {

    /**
     * Creates a Checkout Pro preference.
     *
     * @param accessToken    MP access token (Bearer)
     * @param preferenceBody the preference payload as a map
     * @return the init_point URL string
     */
    String createPreference(String accessToken, Map<String, Object> preferenceBody);

    /**
     * Fetches a payment by ID.
     *
     * @param accessToken MP access token (Bearer)
     * @param paymentId   the payment ID from the webhook notification
     * @return payment data map including status and external_reference
     */
    Map<String, Object> getPayment(String accessToken, String paymentId);
}
