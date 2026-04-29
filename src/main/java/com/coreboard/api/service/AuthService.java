package com.coreboard.api.service;

import com.coreboard.api.domain.dto.auth.AuthRequest;
import com.coreboard.api.domain.dto.auth.GoogleAuthRequest;
import com.coreboard.api.domain.entity.Employee;
import com.coreboard.api.exception.ExternalIntegrationException;
import com.coreboard.api.security.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmployeeService employeeService;

    @Value("${api.security.google.client-id}")
    private String googleClientId;

    public String login(AuthRequest authRequest) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authRequest.email(),
                authRequest.password());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        Employee employee = employeeService.findEntityByEmail(authentication.getName());

        return jwtService.generateToken(employee);
    }

    public String loginGoogle(GoogleAuthRequest googleAuthRequest) {

        GoogleIdToken idToken;

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        try {
            idToken = verifier.verify(googleAuthRequest.token());
        }
        catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
            throw new ExternalIntegrationException("Erro ao validar token com o Google: " + e.getMessage());
        }

        if (idToken == null) {
            throw new AccessDeniedException("Token do Google inválido ou expirado.");
        }

        String email = idToken.getPayload().getEmail();
        Employee employee = employeeService.findEntityByEmail(email);


        return jwtService.generateToken(employee);
    }

}
