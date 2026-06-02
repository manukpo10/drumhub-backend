package com.drumhub.payment.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * Fetches the MEP (bolsa) dollar rate from dolarapi.com.
 * All failures are caught and return Optional.empty() — this component never throws.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DolarApiClient implements DollarRateProvider {

    private static final String DOLAR_API_URL = "https://dolarapi.com/v1/dolares/bolsa";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(TIMEOUT)
            .build();

    @Override
    public Optional<Double> fetchMep() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DOLAR_API_URL))
                    .GET()
                    .timeout(TIMEOUT)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                log.warn("DolarApi returned HTTP {} — skipping rate update", response.statusCode());
                return Optional.empty();
            }

            Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {});

            Object venta = body.get("venta");
            if (venta == null) {
                venta = body.get("compra");
            }
            if (venta == null) {
                log.warn("DolarApi response missing both 'venta' and 'compra' fields");
                return Optional.empty();
            }

            double rate = ((Number) venta).doubleValue();
            return Optional.of(rate);

        } catch (Exception e) {
            log.warn("Failed to fetch MEP rate from dolarapi.com: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
