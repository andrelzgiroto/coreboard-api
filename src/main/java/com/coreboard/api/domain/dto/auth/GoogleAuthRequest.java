package com.coreboard.api.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload for authentication via Google provider")
public record GoogleAuthRequest(

        @Schema(description = "JWT token or ID Token provided by Google", example = "eyJhbGciOiJSUzI1NiIsImtpZ...")
        @NotBlank(message = "Google token is required.")
        String token
) {}