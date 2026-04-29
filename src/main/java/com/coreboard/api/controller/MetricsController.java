package com.coreboard.api.controller;

import com.coreboard.api.controller.openapi.MetricsOpenApi;
import com.coreboard.api.domain.dto.metrics.CustomerMetricsResponse;
import com.coreboard.api.domain.dto.metrics.EmployeeMetricsResponse;
import com.coreboard.api.domain.dto.metrics.MetricsDashboardResponse;
import com.coreboard.api.domain.dto.metrics.MetricsFilter;
import com.coreboard.api.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
public class MetricsController implements MetricsOpenApi {

    private final MetricsService metricsService;

    @GetMapping
    public ResponseEntity<MetricsDashboardResponse> metricsDashboard(@ModelAttribute MetricsFilter metricsFilter) {
        MetricsDashboardResponse response = metricsService.metricsDashboard(metricsFilter);

        return ResponseEntity.ok(response);
    }

    @GetMapping("employees")
    public ResponseEntity<Page<EmployeeMetricsResponse>> employeeDashboard(
            @ModelAttribute MetricsFilter metricsFilter,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "18") Integer size
    ) {
        Page<EmployeeMetricsResponse> response = metricsService.employeeDashboard(metricsFilter, page, size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("customers")
    public ResponseEntity<Page<CustomerMetricsResponse>> customerDashboard(
            @ModelAttribute MetricsFilter metricsFilter,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "18") Integer size
    ) {
        Page<CustomerMetricsResponse> response = metricsService.customerDashboard(metricsFilter, page, size);

        return ResponseEntity.ok(response);
    }
}
