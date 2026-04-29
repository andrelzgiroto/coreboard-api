package com.coreboard.api.domain.entity;

import com.coreboard.api.domain.enums.ServiceOrderStatus;
import com.coreboard.api.exception.BusinessException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "service_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 15)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "service_title", nullable = false, length = 200)
    private String serviceTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_order_status", length = 30)
    private ServiceOrderStatus serviceOrderStatus;

    @Column(name = "total_value", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalValue;

    @OneToOne(mappedBy = "serviceOrder", fetch = FetchType.LAZY)
    private PaymentReceipt paymentReceipt;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id", nullable = false)
    private Employee assignedEmployee;

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    public void start() {
        if (serviceOrderStatus != ServiceOrderStatus.OPEN) {
            throw new BusinessException("Only OPEN orders can be started.");
        }
        serviceOrderStatus = ServiceOrderStatus.IN_PROCESS;
    }

    public void finish() {
        if (serviceOrderStatus != ServiceOrderStatus.IN_PROCESS) {
            throw new BusinessException("Only IN PROCESS orders can be finished");
        }
        serviceOrderStatus = ServiceOrderStatus.FINISHED;
        finishedAt = LocalDateTime.now();
    }

    public void deliver() {
        if (serviceOrderStatus != ServiceOrderStatus.FINISHED) {
            throw new BusinessException("Only FINISHED orders can be delivered");
        }

        if (paymentReceipt == null) {
            throw new BusinessException("Only PAID orders can be delivered");
        }

        serviceOrderStatus = ServiceOrderStatus.DELIVERED;
        deliveredAt = LocalDateTime.now();
    }

    public void cancel() {
        if (serviceOrderStatus != ServiceOrderStatus.OPEN && serviceOrderStatus != ServiceOrderStatus.IN_PROCESS) {
            throw new BusinessException("Only OPEN or IN PROCESS orders can be cancelled");
        }

        serviceOrderStatus = ServiceOrderStatus.CANCELLED;
    }
}
