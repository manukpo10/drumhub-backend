package com.drumhub.payment.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MercadoPagoClientImpl implements MercadoPagoClient {

    private static final String MP_BASE_URL = "https://api.mercadopago.com";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(TIMEOUT)
            .build();

    @Override
    public String createPreference(String accessToken, Map<String, Object> preferenceBody) {
        try {
            String requestBody = objectMapper.writeValueAsString(preferenceBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(MP_BASE_URL + "/checkout/preferences"))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .timeout(TIMEOUT)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("MP API error creating preference: HTTP " + response.statusCode() + " — " + response.body());
            }

            Map<String, Object> responseMap = objectMapper.readValue(
                    response.body(),
                    new TypeReference<>() {}
            );

            Object initPoint = responseMap.get("init_point");
            if (initPoint == null) {
                throw new RuntimeException("MP API response missing init_point");
            }
            return initPoint.toString();

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create MP preference", e);
        }
    }

    @Override
    public Map<String, Object> getPayment(String accessToken, String paymentId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(MP_BASE_URL + "/v1/payments/" + paymentId))
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .timeout(TIMEOUT)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("MP API error fetching payment " + paymentId + ": HTTP " + response.statusCode());
            }

            return objectMapper.readValue(response.body(), new TypeReference<>() {});

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch MP payment " + paymentId, e);
        }
    }
}
