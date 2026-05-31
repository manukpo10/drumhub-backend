package com.drumhub.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 50)
        @Pattern(regexp = "^[a-z0-9_]+$", message = "Only lowercase letters, numbers and underscores")
        String username,

        @NotBlank @Size(max = 100)
        String name,

        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 8, max = 100)
        String password,

        @Size(max = 50)
        String avatarSeed
) {}
