package com.coreboard.api.domain.dto.serviceorder;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Payload for opening a new Service Order (SO)")
public record ServiceOrderRequest(

        @Schema(description = "ID of the customer requesting the service")
        @NotNull(message = "Customer ID is required.")
        UUID customerId,

        @Schema(description = "Short title identifying the service/problem", example = "iPhone 13 screen replacement")
        @NotBlank(message = "Service title is required.")
        String serviceTitle,

        @Schema(description = "Full report of the problem and equipment conditions", example = "Device arrived with a cracked screen and won't turn on.")
        @NotBlank(message = "Description is required.")
        String description,

        @Schema(description = "Approved initial budget", example = "850.00")
        @NotNull(message = "Total value is required.")
        BigDecimal totalValue,

        @Schema(description = "ID of the technician assigned to start the service")
        @NotNull(message = "Assigned Employee ID is required.")
        UUID assignedEmployeeId,

        @Schema(description = "Internally visible notes only (device passwords, previous damage)", example = "Screen password: 123456")
        String internalNotes
) {}