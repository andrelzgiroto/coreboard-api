package com.coreboard.api.domain.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload for updating customer registration data (excludes CPF)")
public record UpdateCustomerRequest(

        @Schema(description = "Updated customer name", example = "John Doe Updated")
        @NotBlank(message = "Name is required.")
        @Size(max = 150)
        String name,

        @Schema(description = "New contact phone", example = "+55 11 91111-2222")
        @NotBlank(message = "Phone is required.")
        @Size(max = 20)
        String phone,

        @Schema(description = "New contact email", example = "john.new@email.com")
        @NotBlank(message = "Email is required.")
        @Size(max = 100)
        @Email
        String email
) {}