package com.coreboard.api.domain.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Schema(description = "Complete representation of a customer's data")
public record CustomerResponse(
        @Schema(description = "Unique customer ID in the system")
        UUID id,

        @Schema(description = "Customer's full name", example = "John Doe")
        String name,

        @Schema(description = "Registered phone number", example = "+55 11 98765-4321")
        String phone,

        @Schema(description = "Registered email", example = "john.doe@email.com")
        String email,

        @Schema(description = "Customer's identification document", example = "123.456.789-00")
        String cpf,

        @Schema(description = "Date and time of system registration")
        LocalDateTime createdAt
) {}