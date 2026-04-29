package com.coreboard.api.controller.openapi;

import com.coreboard.api.domain.dto.metrics.CustomerMetricsResponse;
import com.coreboard.api.domain.dto.metrics.EmployeeMetricsResponse;
import com.coreboard.api.domain.dto.metrics.MetricsDashboardResponse;
import com.coreboard.api.domain.dto.metrics.MetricsFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "Metrics & Dashboards", description = "Endpoints for operational and financial performance analysis")
public interface MetricsOpenApi {

    @Operation(summary = "Retrieves the consolidated financial and operational metrics overview")
    ResponseEntity<MetricsDashboardResponse> metricsDashboard(MetricsFilter metricsFilter);

    @Operation(summary = "Retrieves productivity metrics and ranking per employee")
    ResponseEntity<Page<EmployeeMetricsResponse>> employeeDashboard(
            MetricsFilter metricsFilter, Integer page, Integer size);

    @Operation(summary = "Retrieves financial metrics and ranking per customer")
    ResponseEntity<Page<CustomerMetricsResponse>> customerDashboard(
            MetricsFilter metricsFilter, Integer page, Integer size);
}