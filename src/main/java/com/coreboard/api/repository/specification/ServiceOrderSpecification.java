package com.coreboard.api.repository.specification;

import com.coreboard.api.domain.entity.ServiceOrder;
import com.coreboard.api.domain.enums.ServiceOrderStatus;
import com.coreboard.api.util.FilterUtils;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ServiceOrderSpecification {

    public static Specification<ServiceOrder> byCodeStartsWith(String code) {
        return (root, query, cb) -> {
            if (code == null) {
                return cb.conjunction();
            }

            return cb.like(cb.lower(root.get("code")), FilterUtils.startsWith(code));
        };
    }

    public static Specification<ServiceOrder> byCustomerId(UUID customerId) {
        return (root, query, cb) -> {
            if (customerId == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("customer").get("id"), customerId);
        };
    }

    public static Specification<ServiceOrder> byServiceTitleLike(String serviceTitle) {
        return (root, query, cb) -> {
            if (serviceTitle == null) {
                return cb.conjunction();
            }

            return cb.like(cb.lower(root.get("serviceTitle")), FilterUtils.like(serviceTitle));
        };
    }

    public static Specification<ServiceOrder> byServiceOrderStatus(ServiceOrderStatus serviceOrderStatus) {
        return (root, query, cb) -> {
            if (serviceOrderStatus == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("serviceOrderStatus"), serviceOrderStatus);
        };
    }

    public static Specification<ServiceOrder> bytotalValueMin(BigDecimal totalValueMin) {
        return (root, query, cb) -> {
            if (totalValueMin == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(root.get("totalValue"), totalValueMin);
        };
    }

    public static Specification<ServiceOrder> bytotalValueMax(BigDecimal totalValueMax) {
        return (root, query, cb) -> {
            if (totalValueMax == null) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(root.get("totalValue"), totalValueMax);
        };
    }

    public static Specification<ServiceOrder> hasPayment(Boolean hasPayment) {
        return (root, query, cb) -> {
            if (hasPayment == null) {
                return cb.conjunction();
            }

            if (hasPayment) {
                return cb.isNotNull(root.get("paymentReceipt"));
            }
            else {
                return cb.isNull(root.get("paymentReceipt"));
            }
        };
    }

    public static Specification<ServiceOrder> createdAtFrom(LocalDateTime from) {
        return (root, query, cb) -> {
            if (from == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
        };
    }

    public static Specification<ServiceOrder> createdAtTo(LocalDateTime to) {
        return (root, query, cb) -> {
            if (to == null) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(root.get("createdAt"), to);
        };
    }

    public static Specification<ServiceOrder> finishedAtFrom(LocalDateTime from) {
        return (root, query, cb) -> {
            if (from == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(root.get("finishedAt"), from);
        };
    }

    public static Specification<ServiceOrder> finishedAtTo(LocalDateTime to) {
        return (root, query, cb) -> {
            if (to == null) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(root.get("finishedAt"), to);
        };
    }



    public static Specification<ServiceOrder> byAssignedEmployeeId(UUID assignedEmployeeId) {
        return (root, query, cb) -> {
            if (assignedEmployeeId == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("assignedEmployee").get("id"), assignedEmployeeId);
        };
    }
}
