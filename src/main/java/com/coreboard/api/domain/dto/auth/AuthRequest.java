package com.coreboard.api.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload for standard authentication with email and password")
public record AuthRequest(

        @Schema(description = "User's corporate email", example = "admin@coreboard.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "Access password", example = "SecurePassword123")
        @NotBlank
        String password
) {}