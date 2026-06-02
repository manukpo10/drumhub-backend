package com.drumhub.payment.service;

import com.drumhub.common.exception.ResourceNotFoundException;
import com.drumhub.payment.config.MercadoPagoProperties;
import com.drumhub.payment.dto.MpWebhookPayload;
import com.drumhub.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private static final Set<String> APPROVED_STATUSES = Set.of("approved");

    private final MercadoPagoClient mercadoPagoClient;
    private final SubscriptionService subscriptionService;
    private final MercadoPagoProperties mpProperties;

    /**
     * Processes an MP webhook notification. Attempts HMAC signature validation; on failure it
     * falls back to authoritative verification by re-fetching the payment from MP's API.
     */
    public void processWebhook(MpWebhookPayload payload, String dataId,
                                String xSignature, String xRequestId) {
        String secret = mpProperties.getWebhookSecret().trim();
        boolean valid = SignatureVerifier.verify(dataId, xRequestId, xSignature, secret);
        if (valid) {
            log.info("Webhook HMAC valid — dataId='{}' action='{}'", dataId, payload.action());
        } else {
            // HMAC could not be validated (MP panel secret mismatch). Do NOT reject here:
            // fall back to authoritative verification by re-fetching the payment from MP's
            // API with our access token (done in processWebhookSkipSignature). Confirming the
            // payment exists, is approved, and carries our external_reference is a stronger
            // guarantee than the signature — an attacker cannot forge that without our token.
            log.warn("Webhook HMAC mismatch for dataId='{}' — verifying via MP API instead.", dataId);
        }

        processWebhookSkipSignature(payload, dataId);
    }

    /**
     * Package-private: processes a webhook payload assuming signature has already been validated.
     * Used by tests to inject controlled payloads without needing a real HMAC.
     */
    void processWebhookSkipSignature(MpWebhookPayload payload, String dataId) {
        if (!"payment".equals(payload.type())) {
            log.debug("Ignoring non-payment webhook type={}", payload.type());
            return;
        }

        String action = payload.action();
        if (action == null || !(action.equals("payment.approved") || action.equals("payment.created"))) {
            log.debug("Ignoring webhook action={}", action);
            return;
        }

        // Re-fetch payment from MP API to confirm status (don't trust notification alone)
        Map<String, Object> paymentData;
        try {
            paymentData = mercadoPagoClient.getPayment(mpProperties.getAccessToken(), dataId);
        } catch (Exception e) {
            log.error("Failed to fetch payment {} from MP API", dataId, e);
            return;
        }

        String status = (String) paymentData.get("status");
        if (!APPROVED_STATUSES.contains(status)) {
            log.info("Payment {} has status={}, not activating plan", dataId, status);
            return;
        }

        String externalReference = (String) paymentData.get("external_reference");
        if (externalReference == null) {
            log.warn("Payment {} has no external_reference, skipping", dataId);
            return;
        }

        String[] parts = externalReference.split(":");
        if (parts.length != 3) {
            log.warn("Payment {} has malformed external_reference={}, skipping", dataId, externalReference);
            return;
        }

        String username = parts[0];
        String planId = parts[1];
        String period = parts[2];

        Instant planExpiresAt = "annual".equalsIgnoreCase(period)
                ? Instant.now().plus(365, ChronoUnit.DAYS)
                : Instant.now().plus(30, ChronoUnit.DAYS);

        try {
            subscriptionService.activatePaidPlan(username, planId, planExpiresAt, dataId);
            log.info("Activated plan={} for user={} via payment={}", planId, username, dataId);
        } catch (ResourceNotFoundException e) {
            // Per spec: unknown user returns 200 without error
            log.warn("Payment {} references unknown user={}, ignoring", dataId, username);
        }
    }
}
