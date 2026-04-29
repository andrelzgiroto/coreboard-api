package com.coreboard.api.domain.dto.metrics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(description = "Consolidated financial metrics for the selected period")
public record FinancialMetrics(

        @Schema(description = "Total value generated from all non-cancelled Service Orders", example = "15500.00")
        BigDecimal totalGenerated,

        @Schema(description = "Total amount successfully received via Payment Receipts", example = "12000.00")
        BigDecimal totalReceived,

        @Schema(description = "Total value of FINISHED Service Orders that have no associated Payment Receipt", example = "3500.00")
        BigDecimal totalPending,

        @Schema(description = "Average value (Ticket) calculated from FINISHED and DELIVERED Service Orders", example = "850.50")
        BigDecimal averageTicket
) {}