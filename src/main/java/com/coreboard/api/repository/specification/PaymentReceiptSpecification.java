package com.coreboard.api.repository.specification;

import com.coreboard.api.domain.entity.PaymentReceipt;
import com.coreboard.api.domain.enums.PaymentMethod;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentReceiptSpecification {

    public static Specification<PaymentReceipt> byServiceOrderId(UUID serviceOrderId) {
        return (root, query, cb) -> {
            if (serviceOrderId == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("serviceOrder").get("id"), serviceOrderId);
        };
    }

    public static Specification<PaymentReceipt> byAmountMin(BigDecimal amountMin) {
        return (root, query, cb) -> {
            if (amountMin == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(root.get("amount"), amountMin);

        };
    }

    public static Specification<PaymentReceipt> byAmountMax(BigDecimal amountMax) {
        return (root, query, cb) -> {
            if (amountMax == null) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(root.get("amount"), amountMax);

        };
    }

    public static Specification<PaymentReceipt> byPaymentMethod(PaymentMethod paymentMethod) {
        return (root, query, cb) -> {
            if (paymentMethod == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("paymentMethod"), paymentMethod);
        };
    }

    public static Specification<PaymentReceipt> receivedAtFrom(LocalDateTime receivedAtFrom) {
        return (root, query, cb) -> {
            if (receivedAtFrom == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("receivedAt"), receivedAtFrom);
        };
    }

    public static Specification<PaymentReceipt> receivedAtTo(LocalDateTime receivedAtTo) {
        return (root, query, cb) -> {
            if (receivedAtTo == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("receivedAt"), receivedAtTo);
        };
    }


}
