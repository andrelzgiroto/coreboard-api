package com.coreboard.api.domain.dto.paymentreceipt;

import com.coreboard.api.domain.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Consolidated representation of a generated financial receipt")
public record PaymentReceiptResponse(
        @Schema(description = "Receipt ID")
        UUID id,

        @Schema(description = "Associated Service Order ID")
        UUID serviceOrderId,

        @Schema(description = "Received amount", example = "150.00")
        BigDecimal amount,

        @Schema(description = "Payment method", example = "PIX")
        PaymentMethod paymentMethod,

        @Schema(description = "Registered date of the financial receipt")
        LocalDateTime receivedAt,

        @Schema(description = "Transaction notes", example = "Advance payment of 50%")
        String notes,

        @Schema(description = "System registration date")
        LocalDateTime createdAt
) {}