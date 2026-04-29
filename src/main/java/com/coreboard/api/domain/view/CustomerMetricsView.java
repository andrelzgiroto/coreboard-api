package com.coreboard.api.domain.view;

import java.math.BigDecimal;
import java.util.UUID;

public interface CustomerMetricsView {
    UUID getCustomerId();
    String getCustomerName();
    Long getTotalOrders();
    BigDecimal getGeneratedValue();
}
