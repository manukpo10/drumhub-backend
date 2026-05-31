package com.drumhub.subscription.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePlanRequest(
        @NotBlank String plan
) {}
