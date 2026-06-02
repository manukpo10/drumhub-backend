package com.drumhub.payment.web;

import com.drumhub.common.exception.BadRequestException;
import com.drumhub.common.web.ApiResponse;
import com.drumhub.payment.dto.MpWebhookPayload;
import com.drumhub.payment.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Tag(name = "Webhooks", description = "Mercado Pago webhook receiver")
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping("/mp")
    @Operation(summary = "Receive Mercado Pago payment notifications")
    public ResponseEntity<ApiResponse<Void>> receiveWebhook(
            @RequestBody MpWebhookPayload payload,
            @RequestParam(name = "data.id", required = false) String queryDataId,
            @RequestHeader(name = "X-Signature", required = false, defaultValue = "") String xSignature,
            @RequestHeader(name = "X-Request-Id", required = false, defaultValue = "") String xRequestId,
            HttpServletRequest request
    ) {
        String bodyDataId = (payload.data() != null && payload.data().id() != null) ? payload.data().id() : "";

        // MP signs the manifest with the data.id from the URL query string (lowercased).
        // Prefer the query param; fall back to the body value when absent.
        String dataId = (queryDataId != null && !queryDataId.isBlank()) ? queryDataId : bodyDataId;

        log.info("MP webhook received — queryString='{}' queryDataId='{}' bodyDataId='{}' resolvedDataId='{}'",
                request.getQueryString(), queryDataId, bodyDataId, dataId);

        webhookService.processWebhook(payload, dataId, xSignature, xRequestId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Received"));
    }
}
