package com.coreboard.api.repository;

import com.coreboard.api.domain.entity.PaymentReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, UUID>, JpaSpecificationExecutor<PaymentReceipt> {

    @Query("""
            SELECT SUM(pr.amount)
            FROM PaymentReceipt pr
            JOIN ServiceOrder so ON pr.serviceOrder.id = so.id
            WHERE pr.receivedAt >= :from
            AND pr.receivedAt <= :to
            AND (:assignedEmployeeId IS NULL OR :assignedEmployeeId = so.assignedEmployee.id)
            AND (:customerId IS NULL OR :customerId = so.customer.id)
            """)
    BigDecimal totalReceived(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("assignedEmployeeId") String assignedEmployeeId,
            @Param("customerId") String customerId
    );

}
