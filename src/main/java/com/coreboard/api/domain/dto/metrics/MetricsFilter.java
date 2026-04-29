package com.coreboard.api.domain.dto.metrics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

import java.time.LocalDateTime;

@With
@Schema(description = "Filter parameters for querying dashboard metrics")
public record MetricsFilter(

        @Schema(description = "Start date of the analysis period", example = "2026-04-01T00:00:00")
        LocalDateTime from,

        @Schema(description = "End date of the analysis period", example = "2026-04-30T23:59:59")
        LocalDateTime to,

        @Schema(description = "Optional filter to analyze metrics of a specific employee")
        String assignedEmployeeId,

        @Schema(description = "Optional filter to analyze metrics of a specific customer")
        String customerId
) {}