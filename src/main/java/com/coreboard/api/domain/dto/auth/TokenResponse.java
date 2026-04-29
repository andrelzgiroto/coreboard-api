package com.coreboard.api.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing access credentials after a successful login")
public record TokenResponse(
        @Schema(description = "JWT token for route authorization", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6...")
        String token,

        @Schema(description = "Token type", example = "Bearer")
        String tipo
) {}