package com.coreboard.api.controller;

import com.coreboard.api.controller.openapi.MetricsOpenApi;
import com.coreboard.api.controller.openapi.PaymentReceiptOpenApi;
import com.coreboard.api.domain.dto.paymentreceipt.PaymentReceiptRequest;
import com.coreboard.api.domain.dto.paymentreceipt.PaymentReceiptResponse;
import com.coreboard.api.domain.enums.PaymentMethod;
import com.coreboard.api.domain.enums.SortBy;
import com.coreboard.api.service.PaymentReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment-receipts")
@RequiredArgsConstructor
public class PaymentReceiptController implements PaymentReceiptOpenApi {

    private final PaymentReceiptService paymentReceiptService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<PaymentReceiptResponse> registerPayment(@Valid @RequestBody PaymentReceiptRequest paymentReceiptRequest) {
        PaymentReceiptResponse response = paymentReceiptService.registerPayment(paymentReceiptRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<PaymentReceiptResponse> findById(@PathVariable("id") UUID id) {
        PaymentReceiptResponse response = paymentReceiptService.findById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<Page<PaymentReceiptResponse>> findAll(
            @RequestParam(required = false) UUID byServiceOrderId,
            @RequestParam(required = false) BigDecimal amountMin,
            @RequestParam(required = false) BigDecimal amountMax,
            @RequestParam(required = false) PaymentMethod paymentMethod,
            @RequestParam(required = false) LocalDateTime receivedAtFrom,
            @RequestParam(required = false) LocalDateTime receivedAtTo,
            @RequestParam(required = false, defaultValue = "MOST_RECENT") SortBy sortBy,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "18") Integer size
    ) {
        Page<PaymentReceiptResponse> response = paymentReceiptService.findAll(
                byServiceOrderId,
                amountMin,
                amountMax,
                paymentMethod,
                receivedAtFrom,
                receivedAtTo,
                sortBy,
                page,
                size);

        return ResponseEntity.ok(response);
    }
}
