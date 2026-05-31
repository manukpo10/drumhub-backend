package com.drumhub.payment.web;

import com.drumhub.common.exception.BadRequestException;
import com.drumhub.payment.dto.MpWebhookPayload;
import com.drumhub.payment.service.WebhookService;
import com.drumhub.security.JwtAuthFilter;
import com.drumhub.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = WebhookController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.drumhub.common.exception.GlobalExceptionHandler.class)
class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WebhookService webhookService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    private static final String VALID_SIGNATURE = "ts=1700000000,v1=abc123";
    private static final String REQUEST_ID = "req-456";

    private MpWebhookPayload buildPayload(String paymentId) {
        return new MpWebhookPayload(
                "notif-1", "payment", "payment.approved",
                new MpWebhookPayload.MpWebhookData(paymentId)
        );
    }

    @Test
    void webhookEndpoint_withValidSignature_returns200() throws Exception {
        MpWebhookPayload payload = buildPayload("12345");
        doNothing().when(webhookService).processWebhook(any(), anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/webhooks/mp")
                        .param("data.id", "12345")
                        .header("X-Signature", VALID_SIGNATURE)
                        .header("X-Request-Id", REQUEST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    void webhookEndpoint_withInvalidSignature_returns400() throws Exception {
        MpWebhookPayload payload = buildPayload("12345");
        doThrow(new BadRequestException("Invalid signature"))
                .when(webhookService).processWebhook(any(), anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/webhooks/mp")
                        .param("data.id", "12345")
                        .header("X-Signature", "ts=bad,v1=bad")
                        .header("X-Request-Id", REQUEST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void webhookEndpoint_withoutAuth_returns200WithValidSignature() throws Exception {
        // Endpoint is public — no auth header needed
        MpWebhookPayload payload = buildPayload("12345");
        doNothing().when(webhookService).processWebhook(any(), anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/webhooks/mp")
                        .param("data.id", "12345")
                        .header("X-Signature", VALID_SIGNATURE)
                        .header("X-Request-Id", REQUEST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }
}
