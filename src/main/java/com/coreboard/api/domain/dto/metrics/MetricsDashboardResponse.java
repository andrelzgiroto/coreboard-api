package com.coreboard.api.domain.dto.metrics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Complete dashboard aggregating financial and operational data")
public record MetricsDashboardResponse(

        @Schema(description = "Financial overview section")
        FinancialMetrics financial,

        @Schema(description = "Operational efficiency section")
        OperationalMetrics operational
) {}