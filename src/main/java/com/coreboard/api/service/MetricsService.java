package com.coreboard.api.service;

import com.coreboard.api.domain.dto.metrics.*;
import com.coreboard.api.repository.PaymentReceiptRepository;
import com.coreboard.api.repository.ServiceOrderRepository;
import com.coreboard.api.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final PaymentReceiptRepository paymentReceiptRepository;

    @Transactional(readOnly = true)
    public MetricsDashboardResponse metricsDashboard(MetricsFilter metricsFilter) {

        metricsFilter = handleFilterDate(metricsFilter);

        FinancialMetrics financialMetrics = financialMetricsBuilder(metricsFilter);
        OperationalMetrics operationalMetrics = operationalMetricsBuilder(metricsFilter);

        return MetricsDashboardResponse
                .builder()
                .financial(financialMetrics)
                .operational(operationalMetrics)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<EmployeeMetricsResponse> employeeDashboard(
            MetricsFilter metricsFilter,
            Integer page,
            Integer size) {

        metricsFilter = handleFilterDate(metricsFilter);
        Pageable pageable = PageableUtils.create(page, size);

        return employeeMetricsBuilder(metricsFilter, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CustomerMetricsResponse> customerDashboard(
            MetricsFilter metricsFilter,
            Integer page,
            Integer size) {

        metricsFilter = handleFilterDate(metricsFilter);
        Pageable pageable = PageableUtils.create(page, size);

        return customerMetricsBuilder(metricsFilter, pageable);
    }

    private FinancialMetrics financialMetricsBuilder(MetricsFilter metricsFilter) {
        BigDecimal totalGenerated = Optional.ofNullable(
                serviceOrderRepository.totalGenerated(
                metricsFilter.from(),
                metricsFilter.to(),
                metricsFilter.assignedEmployeeId(),
                metricsFilter.customerId()
                )
        ).orElse(BigDecimal.ZERO);



        BigDecimal totalReceived = Optional.ofNullable(
                paymentReceiptRepository.totalReceived(
                        metricsFilter.from(),
                        metricsFilter.to(),
                        metricsFilter.assignedEmployeeId(),
                        metricsFilter.customerId()
                )
        ).orElse(BigDecimal.ZERO);

        BigDecimal totalPending = Optional.ofNullable(
                serviceOrderRepository.totalPending(
                        metricsFilter.from(),
                        metricsFilter.to(),
                        metricsFilter.assignedEmployeeId(),
                        metricsFilter.customerId()
                )
        ).orElse(BigDecimal.ZERO);

        BigDecimal averageTicket = Optional.ofNullable(
                serviceOrderRepository.averageTicket(
                        metricsFilter.from(),
                        metricsFilter.to(),
                        metricsFilter.assignedEmployeeId(),
                        metricsFilter.customerId()
                )
        ).orElse(BigDecimal.ZERO);

        return FinancialMetrics
                .builder()
                .totalGenerated(totalGenerated)
                .totalReceived(totalReceived)
                .totalPending(totalPending)
                .averageTicket(averageTicket)
                .build();
    }

    private OperationalMetrics operationalMetricsBuilder(MetricsFilter metricsFilter) {
        Long totalOpen = serviceOrderRepository.totalOpen(
                metricsFilter.from(),
                metricsFilter.to(),
                metricsFilter.assignedEmployeeId(),
                metricsFilter.customerId()
        );

        Long totalInProcess = serviceOrderRepository.totalInProcess(
                metricsFilter.from(),
                metricsFilter.to(),
                metricsFilter.assignedEmployeeId(),
                metricsFilter.customerId()
        );

        Long totalFinished = serviceOrderRepository.totalFinished(
                metricsFilter.from(),
                metricsFilter.to(),
                metricsFilter.assignedEmployeeId(),
                metricsFilter.customerId()
        );

        Long totalDelivered = serviceOrderRepository.totalDelivered(
                metricsFilter.from(),
                metricsFilter.to(),
                metricsFilter.assignedEmployeeId(),
                metricsFilter.customerId()
        );

        Double averageExecutionTime = Optional.ofNullable(
                serviceOrderRepository.averageExecutionTime(
                        metricsFilter.from(),
                        metricsFilter.to(),
                        metricsFilter.assignedEmployeeId(),
                        metricsFilter.customerId()
                )
        ).orElse(0.00);

        BigDecimal averageExecutionTimeFormatted = BigDecimal
                .valueOf(averageExecutionTime)
                .setScale(2, RoundingMode.HALF_UP);

        LocalDateTime limitDate = LocalDateTime.now().minusDays(5);
        Long overdueOrders = serviceOrderRepository.overdueOrders(
                limitDate,
                metricsFilter.assignedEmployeeId(),
                metricsFilter.customerId()
        );

      return OperationalMetrics
              .builder()
              .totalOpen(totalOpen)
              .totalInProcess(totalInProcess)
              .totalFinished(totalFinished)
              .totalDelivered(totalDelivered)
              .averageExecutionTimeHours(averageExecutionTimeFormatted)
              .overdueOrders(overdueOrders)
              .build();
    }

    private Page<EmployeeMetricsResponse> employeeMetricsBuilder(MetricsFilter metricsFilter, Pageable pageable) {
        return serviceOrderRepository.byEmployee(
                metricsFilter.from(),
                metricsFilter.to(),
                metricsFilter.assignedEmployeeId(),
                metricsFilter.customerId(),
                pageable)
                .map(emp -> EmployeeMetricsResponse
                        .builder()
                        .employeeId(emp.getEmployeeId())
                        .employeeName(emp.getEmployeeName())
                        .totalOrders(emp.getTotalOrders())
                        .generatedValue(
                                Optional
                                        .ofNullable(emp.getGeneratedValue())
                                        .orElse(BigDecimal.ZERO))
                        .receivedValue(
                                Optional
                                .ofNullable(emp.getReceivedValue())
                                .orElse(BigDecimal.ZERO))
                        .build()
                );
    }

    private Page<CustomerMetricsResponse> customerMetricsBuilder(MetricsFilter metricsFilter, Pageable pageable) {
        return serviceOrderRepository.byCustomer(
                metricsFilter.from(),
                metricsFilter.to(),
                metricsFilter.assignedEmployeeId(),
                metricsFilter.customerId(),
                pageable)
                .map(cm -> CustomerMetricsResponse
                        .builder()
                        .customerId(cm.getCustomerId())
                        .customerName(cm.getCustomerName())
                        .totalOrders(cm.getTotalOrders())
                        .generatedValue(
                                Optional
                                .ofNullable(cm.getGeneratedValue())
                                .orElse(BigDecimal.ZERO))
                        .build()
                );
    }

    private MetricsFilter handleFilterDate(MetricsFilter metricsFilter) {

        metricsFilter = metricsFilter
                .withFrom(metricsFilter.from() != null ? metricsFilter.from() : LocalDateTime.of(2000, 1, 1, 0, 0))
                .withTo(metricsFilter.to() != null ? metricsFilter.to() : LocalDateTime.now());

        return metricsFilter;
    }
}
