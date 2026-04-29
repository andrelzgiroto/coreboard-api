package com.coreboard.api.domain.dto.metrics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(description = "Consolidated operational metrics reflecting workflow productivity")
public record OperationalMetrics(

        @Schema(description = "Number of Service Orders currently in OPEN status", example = "15")
        Long totalOpen,

        @Schema(description = "Number of Service Orders currently IN_PROCESS", example = "8")
        Long totalInProcess,

        @Schema(description = "Number of Service Orders FINISHED within the given period", example = "42")
        Long totalFinished,

        @Schema(description = "Number of Service Orders DELIVERED to customers within the given period", example = "38")
        Long totalDelivered,

        @Schema(description = "Average time in hours taken to complete a Service Order (from OPEN to FINISHED/DELIVERED)", example = "24.5")
        BigDecimal averageExecutionTimeHours,

        @Schema(description = "Count of OPEN or IN_PROCESS orders that have surpassed their expected deadline", example = "3")
        Long overdueOrders
) {}