package com.coreboard.api.domain.dto.serviceorder;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Payload for editing the scope of an ongoing SO")
public record UpdateServiceOrderRequest(

        @Schema(description = "New SO title", example = "iPhone 13 screen and battery replacement")
        @NotBlank(message = "Service title is required.")
        String serviceTitle,

        @Schema(description = "Addition of new descriptive details", example = "Customer approved adding a battery to the original service.")
        @NotBlank(message = "Description is required.")
        String description,

        @Schema(description = "New value after budget adjustment", example = "1100.00")
        @NotNull(message = "Total value is required.")
        BigDecimal totalValue,

        @Schema(description = "Addition of new internal notes")
        String internalNotes
) {}