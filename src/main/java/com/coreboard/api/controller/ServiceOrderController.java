package com.coreboard.api.controller;

import com.coreboard.api.controller.openapi.ServiceOrderOpenApi;
import com.coreboard.api.domain.dto.serviceorder.ServiceOrderRequest;
import com.coreboard.api.domain.dto.serviceorder.ServiceOrderResponse;
import com.coreboard.api.domain.dto.serviceorder.UpdateServiceOrderRequest;
import com.coreboard.api.domain.enums.ServiceOrderStatus;
import com.coreboard.api.domain.enums.SortBy;
import com.coreboard.api.service.ServiceOrderService;
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
@RequestMapping("/api/service-orders")
@RequiredArgsConstructor
public class ServiceOrderController implements ServiceOrderOpenApi {

    private final ServiceOrderService serviceOrderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ServiceOrderResponse> create(@Valid @RequestBody ServiceOrderRequest serviceOrderRequest) {
        ServiceOrderResponse response = serviceOrderService.create(serviceOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ServiceOrderResponse> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateServiceOrderRequest updateServiceOrderRequest) {

        ServiceOrderResponse response = serviceOrderService.update(id, updateServiceOrderRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ServiceOrderResponse> findById(@PathVariable("id") UUID id) {
        ServiceOrderResponse response = serviceOrderService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<Page<ServiceOrderResponse>> findAll(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) String serviceTitle,
            @RequestParam(required = false) ServiceOrderStatus serviceOrderStatus,
            @RequestParam(required = false) BigDecimal totalValueMin,
            @RequestParam(required = false) BigDecimal totalValueMax,
            @RequestParam(required = false) Boolean hasPayment,
            @RequestParam(required = false) LocalDateTime createdAtFrom,
            @RequestParam(required = false) LocalDateTime createdAtTo,
            @RequestParam(required = false) UUID assignedEmployeeId,
            @RequestParam(required = false, defaultValue = "MOST_RECENT") SortBy sortBy,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "18") Integer size) {

        Page<ServiceOrderResponse> response = serviceOrderService.findAll(
                code,
                customerId,
                serviceTitle,
                serviceOrderStatus,
                totalValueMin,
                totalValueMax,
                hasPayment,
                createdAtFrom,
                createdAtTo,
                assignedEmployeeId,
                sortBy,
                page,
                size);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        serviceOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/start-process")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ServiceOrderResponse> startProcess(@PathVariable("id") UUID id) {
        ServiceOrderResponse response = serviceOrderService.startProcess(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/finish-process")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ServiceOrderResponse> finishProcess(@PathVariable("id") UUID id) {
        ServiceOrderResponse response = serviceOrderService.finishProcess(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/deliver-process")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ServiceOrderResponse> deliverProcess(@PathVariable("id") UUID id) {
        ServiceOrderResponse response = serviceOrderService.deliverProcess(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel-process")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<ServiceOrderResponse> cancelProcess(@PathVariable("id") UUID id) {
        ServiceOrderResponse response = serviceOrderService.cancelProcess(id);

        return ResponseEntity.ok(response);
    }



}
