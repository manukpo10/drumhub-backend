package com.drumhub.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateAvatarRequest(
        @NotBlank @Size(max = 50) String avatarSeed
) {}
