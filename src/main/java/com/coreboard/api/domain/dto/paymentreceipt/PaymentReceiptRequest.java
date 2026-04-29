package com.coreboard.api.domain.dto.paymentreceipt;

import com.coreboard.api.domain.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Payload for recording a cash receipt")
public record PaymentReceiptRequest(

        @Schema(description = "Service Order ID linked to the payment")
        @NotNull(message = "Service Order ID is required.")
        UUID serviceOrderId,

        @Schema(description = "Amount actually paid", example = "150.00")
        @NotNull(message = "Amount is required.")
        @Size(min = 1, max = 20)
        BigDecimal amount,

        @Schema(description = "Payment method used", example = "PIX")
        @NotNull(message = "Payment method is required.")
        @Size(max = 30)
        PaymentMethod paymentMethod,

        @Schema(description = "Exact date and time of receipt", example = "2026-04-28T10:30:00")
        @NotNull(message = "Date of receipt is required.")
        LocalDateTime receivedAt,

        @Schema(description = "Internal cashier notes", example = "Advance payment of 50%")
        String notes
) {}