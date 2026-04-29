package com.coreboard.api.controller.openapi;

import com.coreboard.api.domain.dto.auth.AuthRequest;
import com.coreboard.api.domain.dto.auth.GoogleAuthRequest;
import com.coreboard.api.domain.dto.auth.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication", description = "Endpoints for login management and JWT token issuance")
public interface AuthOpenApi {

    @Operation(summary = "Authenticates a user via standard credentials")
    ResponseEntity<TokenResponse> login(AuthRequest authRequest);

    @Operation(summary = "Authenticates a user via Google OAuth")
    ResponseEntity<TokenResponse> loginGoogle(GoogleAuthRequest googleAuthRequest);
}