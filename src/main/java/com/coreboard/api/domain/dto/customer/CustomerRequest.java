package com.coreboard.api.domain.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload for registering a new customer")
public record CustomerRequest(

        @Schema(description = "Customer's full name or company name", example = "John Doe")
        @NotBlank(message = "Name is required.")
        @Size(max = 150)
        String name,

        @Schema(description = "Primary contact phone", example = "+55 11 98765-4321")
        @NotBlank(message = "Phone is required.")
        @Size(max = 20)
        String phone,

        @Schema(description = "Valid contact email", example = "john.doe@email.com")
        @NotBlank(message = "Email is required.")
        @Email
        @Size(max = 100)
        String email,

        @Schema(description = "National identification document (CPF or CNPJ)", example = "123.456.789-00")
        @NotBlank(message = "CPF is required.")
        @Size(max = 20)
        String cpf
) {}