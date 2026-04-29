package com.coreboard.api.controller;

import com.coreboard.api.controller.openapi.AuthOpenApi;
import com.coreboard.api.domain.dto.auth.AuthRequest;
import com.coreboard.api.domain.dto.auth.GoogleAuthRequest;
import com.coreboard.api.domain.dto.auth.TokenResponse;
import com.coreboard.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthOpenApi {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody AuthRequest authRequest) {
        String tokenJwt = authService.login(authRequest);

        return ResponseEntity.ok(new TokenResponse(tokenJwt, "Bearer"));
    }

    @PostMapping("/google")
    public ResponseEntity<TokenResponse> loginGoogle(@RequestBody GoogleAuthRequest googleAuthRequest) {
        String tokenJwt = authService.loginGoogle(googleAuthRequest);

        return ResponseEntity.ok(new TokenResponse(tokenJwt, "Bearer"));
    }
}
