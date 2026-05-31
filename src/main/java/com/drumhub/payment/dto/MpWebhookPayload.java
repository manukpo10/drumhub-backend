package com.drumhub.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Mercado Pago webhook notification payload.
 * MP sends: {"id": "...", "type": "payment", "action": "payment.approved", "data": {"id": "12345"}}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MpWebhookPayload(
        String id,
        String type,
        String action,
        MpWebhookData data
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MpWebhookData(String id) {}
}
