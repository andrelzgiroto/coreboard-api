package com.coreboard.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.coreboard.api.domain.entity.Employee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secret;

    private static final String ISSUER = "CoreBoard API";

    public String generateToken(Employee employee) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(employee.getEmail())
                    .withClaim("id", employee.getId().toString())
                    .withClaim("role", employee.getEmployeeType().name())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        }
        catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        }
        catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant generateExpirationDate() {
        // Expires in 12 hours
        return LocalDateTime.now().plusHours(12).toInstant(ZoneOffset.of("-03:00"));
    }
}