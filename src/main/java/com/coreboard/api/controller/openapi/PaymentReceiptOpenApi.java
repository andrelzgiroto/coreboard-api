package com.coreboard.api.controller.openapi;

import com.coreboard.api.domain.dto.paymentreceipt.PaymentReceiptRequest;
import com.coreboard.api.domain.dto.paymentreceipt.PaymentReceiptResponse;
import com.coreboard.api.domain.enums.PaymentMethod;
import com.coreboard.api.domain.enums.SortBy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "Payment Receipts", description = "Endpoints for financial records and Service Order payments")
public interface PaymentReceiptOpenApi {

    @Operation(summary = "Registers a new payment linked to a Service Order")
    ResponseEntity<PaymentReceiptResponse> registerPayment(PaymentReceiptRequest paymentReceiptRequest);

    @Operation(summary = "Retrieves a payment receipt by ID")
    ResponseEntity<PaymentReceiptResponse> findById(@Parameter(description = "Payment Receipt ID") UUID id);

    @Operation(summary = "Retrieves a paginated list of payment receipts with filters")
    ResponseEntity<Page<PaymentReceiptResponse>> findAll(
            UUID byServiceOrderId, BigDecimal amountMin, BigDecimal amountMax,
            PaymentMethod paymentMethod, LocalDateTime receivedAtFrom, LocalDateTime receivedAtTo,
            SortBy sortBy, Integer page, Integer size);
}