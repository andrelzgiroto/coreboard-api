package com.coreboard.api.domain.dto.serviceorder;

import com.coreboard.api.domain.enums.ServiceOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Schema(description = "Detailed representation of the SO lifecycle")
public record ServiceOrderResponse(
        @Schema(description = "Service Order ID")
        UUID id,

        @Schema(description = "Human-readable code for customer/budget", example = "SO-2026-0001")
        String code,

        @Schema(description = "Customer ID")
        UUID customerId,

        @Schema(description = "SO Title", example = "iPhone 13 screen replacement")
        String serviceTitle,

        @Schema(description = "Detailed description")
        String description,

        @Schema(description = "Current SO state in the state machine", example = "IN_PROCESS")
        ServiceOrderStatus serviceOrderStatus,

        @Schema(description = "Total service value", example = "850.00")
        BigDecimal totalValue,

        @Schema(description = "Payment receipt ID if already settled")
        UUID paymentReceiptId,

        @Schema(description = "Opening date")
        LocalDateTime createdAt,

        @Schema(description = "Date the repair/service was technically finished")
        LocalDateTime finishedAt,

        @Schema(description = "Date the device was picked up by the customer")
        LocalDateTime deliveredAt,

        @Schema(description = "ID of the currently responsible employee")
        UUID assignedEmployeeId,

        @Schema(description = "Internal technical notes")
        String internalNotes
) {}