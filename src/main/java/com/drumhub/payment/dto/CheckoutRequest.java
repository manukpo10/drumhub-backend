package com.drumhub.payment.dto;

import jakarta.validation.constraints.NotBlank;

public record CheckoutRequest(
        @NotBlank String planId,
        @NotBlank String period
) {}
