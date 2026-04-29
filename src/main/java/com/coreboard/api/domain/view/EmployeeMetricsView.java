package com.coreboard.api.domain.view;

import java.math.BigDecimal;
import java.util.UUID;

public interface EmployeeMetricsView {
    UUID getEmployeeId();
    String getEmployeeName();
    Long getTotalOrders();
    BigDecimal getGeneratedValue();
    BigDecimal getReceivedValue();
}
