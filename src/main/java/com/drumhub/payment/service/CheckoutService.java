package com.drumhub.payment.service;

import com.drumhub.common.exception.BadRequestException;
import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.payment.config.MercadoPagoProperties;
import com.drumhub.payment.dto.CheckoutRequest;
import com.drumhub.payment.dto.CheckoutResponse;
import com.drumhub.user.domain.User;
import com.drumhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutService {

    private static final Set<String> VALID_PAID_PLANS = Set.of("pro", "studio");

    // ARS prices: key = "planId:period"
    private static final Map<String, Integer> PRICES = Map.of(
            "pro:monthly", 5000,
            "pro:annual", 48000,
            "studio:monthly", 12000,
            "studio:annual", 116000
    );

    private static final Map<String, String> PLAN_NAMES = Map.of(
            "pro", "DrumHub Pro",
            "studio", "DrumHub Studio"
    );

    // Back URLs — in production these should be configured, but hardcoded for simplicity per design
    private static final String BACK_URL_BASE = "https://drumhub.io";

    private final MercadoPagoClient mercadoPagoClient;
    private final MercadoPagoProperties mpProperties;
    private final UserRepository userRepository;

    public CheckoutResponse createCheckout(CheckoutRequest request, String username) {
        String planId = request.planId().toLowerCase();
        String period = request.period().toLowerCase();

        if (!VALID_PAID_PLANS.contains(planId)) {
            throw new BadRequestException("Cannot checkout with plan: " + planId + ". Only 'pro' and 'studio' are allowed.");
        }

        String priceKey = planId + ":" + period;
        Integer unitPrice = PRICES.get(priceKey);
        if (unitPrice == null) {
            throw new BadRequestException("Invalid plan/period combination: " + planId + "/" + period);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        String planName = PLAN_NAMES.getOrDefault(planId, planId);
        String externalReference = username + ":" + planId + ":" + period;

        Map<String, Object> preferenceBody = Map.of(
                "items", List.of(Map.of(
                        "title", planName + " (" + period + ")",
                        "quantity", 1,
                        "unit_price", unitPrice,
                        "currency_id", "ARS"
                )),
                "payer", Map.of("email", user.getEmail()),
                "external_reference", externalReference,
                "back_urls", Map.of(
                        "success", BACK_URL_BASE + "/payment-success",
                        "failure", BACK_URL_BASE + "/payment-failure",
                        "pending", BACK_URL_BASE + "/payment-pending"
                ),
                "auto_return", "approved"
        );

        log.info("Creating MP preference for user={} plan={} period={}", username, planId, period);
        String initPoint = mercadoPagoClient.createPreference(mpProperties.getAccessToken(), preferenceBody);

        return new CheckoutResponse(initPoint);
    }
}
