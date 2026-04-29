package com.coreboard.api.repository;

import com.coreboard.api.domain.entity.ServiceOrder;
import com.coreboard.api.domain.view.CustomerMetricsView;
import com.coreboard.api.domain.view.EmployeeMetricsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, UUID>, JpaSpecificationExecutor<ServiceOrder> {

    @Query(value = "SELECT nextval('service_order_code_seq')", nativeQuery = true)
    long getNextCodeSequence();

    boolean existsByIdAndPaymentReceiptNotNull(UUID id);

    @Query("""
            SELECT SUM(so.totalValue)
            FROM ServiceOrder so
            WHERE so.serviceOrderStatus NOT IN ('CANCELLED')
            AND so.createdAt >= :from
            AND so.createdAt <= :to
            AND (:assignedEmployeeId IS NULL OR :assignedEmployeeId = so.assignedEmployee.id)
            AND (:customerId IS NULL OR :customerId = so.customer.id)
            """)
    BigDecimal totalGenerated(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("assignedEmployeeId") String assignedEmployeeId,
            @Param("customerId") String customerId
    );

    @Query("""
            SELECT SUM(so.totalValue)
            FROM ServiceOrder so
            LEFT JOIN so.paymentReceipt pr
            WHERE pr IS NULL
            AND so.createdAt >= :from
            AND so.createdAt <= :to
            AND (:assignedEmployeeId IS NULL OR :assignedEmployeeId = so.assignedEmployee.id)
            AND (:customerId IS NULL OR :customerId = so.customer.id)
            """)
    BigDecimal totalPending(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("assignedEmployeeId") String assignedEmployeeId,
            @Param("customerId") String customerId
    );

    @Query("""
            SELECT AVG(so.totalValue)
            FROM ServiceOrder so
            WHERE so.serviceOrderStatus IN ('FINISHED', 'DELIVERED')
            AND so.finishedAt >= :from
            AND so.finishedAt <= :to
            AND (:assignedEmployeeId IS NULL OR :assignedEmployeeId = so.assignedEmployee.id)
            AND (:customerId IS NULL OR :customerId = so.customer.id)
            """)
    BigDecimal averageTicket(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("assignedEmployeeId") String assignedEmployeeId,
            @Param("customerId") String customerId
    );

    @Query("""
            SELECT COUNT(so)
            FROM ServiceOrder so
            WHERE so.serviceOrderStatus = 'OPEN'
            AND so.createdAt >= :from
            AND so.createdAt <= :to
            AND (:assignedEmployeeId IS NULL OR :assignedEmployeeId = so.assignedEmployee.id)
            AND (:customerId IS NULL OR :customerId = so.customer.id)
            """)
    Long totalOpen(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("assignedEmployeeId") String assignedEmployeeId,
            @Param("customerId") String customerId
    );

    @Query("""
            SELECT COUNT(so)
            FROM ServiceOrder so
            WHERE so.serviceOrderStatus = 'IN_PROCESS'
            AND so.createdAt >= :from
            AND so.createdAt <= :to
            AND (:assignedEmployeeId IS NULL OR :assignedEmployeeId = so.assignedEmployee.id)
            AND (:customerId IS NULL OR :customerId = so.customer.id)
            """)
    Long totalInProcess(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("assignedEmployeeId") String assignedEmployeeId,
            @Param("customerId") String customerId
    );

    @Query("""
            SELECT COUNT(so)
            FROM ServiceOrder so
            WHERE so.serviceOrderStatus = 'FINISHED'
            AND so.finishedAt >= :from
            AND so.finishedAt <= :to
            AND (:assignedEmployeeId IS NULL OR :assignedEmployeeId = so.assignedEmployee.id)
            AND (:customerId IS NULL OR :customerId = so.customer.id)
            """)
    Long totalFinished(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("assignedEmployeeId") String assignedEmployeeId,
            @Param("customerId") String customerId
    );

    @Query("""
            SELECT COUNT(so)
            FROM ServiceOrder so
            WHERE so.serviceOrderStatus = 'DELIVERED'
            AND so.finishedAt >= :from
            AND so.finishedAt <= :to
            AND (:assignedEmployeeId IS NULL OR :assignedEmployeeId = so.assignedEmployee.id)
            AND (:customerId IS NULL OR :customerId = so.customer.id)
            """)
    Long totalDelivered(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("assignedEmployeeId") String assignedEmployeeId,
            @Param("customerId") String customerId
    );

    @Query("""
            SELECT AVG((EXTRACT(EPOCH FROM so.finishedAt) - EXTRACT(EPOCH FROM so.createdAt)) / 3600)
            FROM ServiceOrder so
            WHERE so.serviceOrderStatus IN ('FINISHED', 'DELIVERED')
            AND so.finishedAt >= :from
            AND so.finishedAt <= :to
            AND (:assignedEmployeeId IS NULL OR :assignedEmployeeId = so.assignedEmployee.id)
            AND (:customerId IS NULL OR :customerId = so.customer.id)
            """)
    Double averageExecutionTime(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("assignedEmployeeId") String assignedEmployeeId,
            @Param("customerId") String customerId
    );


    @Query("""
            SELECT COUNT(so)
            FROM ServiceOrder so
            WHERE so.serviceOrderStatus IN ('OPEN', 'IN_PROCESS')
            AND so.createdAt < :limitDate
            AND (:assignedEmployeeId IS NULL OR :assignedEmployeeId = so.assignedEmployee.id)
            AND (:customerId IS NULL OR :customerId = so.customer.id)
            """)
    Long overdueOrders(
            @Param("limitDate") LocalDateTime limitDate,
            @Param("assignedEmployeeId") String assignedEmployeeId,
            @Param("customerId") String customerId
    );

    @Query("""
    SELECT emp.id AS employeeId,
           emp.name AS employeeName,
           COUNT(so) AS totalOrders,
           SUM(so.totalValue) AS generatedValue,
           SUM(pr.amount) AS receivedValue
    FROM ServiceOrder so
    JOIN so.assignedEmployee emp
    LEFT JOIN so.paymentReceipt pr
    WHERE so.createdAt >= :from
    AND so.createdAt <= :to
    AND (:assignedEmployeeId IS NULL OR so.assignedEmployee.id = :assignedEmployeeId)
    AND (:customerId IS NULL OR so.customer.id = :customerId)
    GROUP BY emp.id, emp.name
    """)
    Page<EmployeeMetricsView> byEmployee(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("assignedEmployeeId") String assignedEmployeeId,
            @Param("customerId") String customerId,
            Pageable pageable
    );

    @Query("""
    SELECT cm.id AS customerId,
           cm.name AS customerName,
           COUNT(so) AS totalOrders,
           SUM(so.totalValue) AS generatedValue
    FROM ServiceOrder so
    JOIN so.customer cm
    WHERE so.createdAt >= :from
    AND so.createdAt <= :to
    AND (:assignedEmployeeId IS NULL OR so.assignedEmployee.id = :assignedEmployeeId)
    AND (:customerId IS NULL OR so.customer.id = :customerId)
    GROUP BY cm.id, cm.name
    """)
    Page<CustomerMetricsView> byCustomer(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("assignedEmployeeId") String assignedEmployeeId,
            @Param("customerId") String customerId,
            Pageable pageable
    );
}
