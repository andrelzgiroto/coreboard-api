package com.coreboard.api.controller.openapi;

import com.coreboard.api.domain.dto.serviceorder.ServiceOrderRequest;
import com.coreboard.api.domain.dto.serviceorder.ServiceOrderResponse;
import com.coreboard.api.domain.dto.serviceorder.UpdateServiceOrderRequest;
import com.coreboard.api.domain.enums.ServiceOrderStatus;
import com.coreboard.api.domain.enums.SortBy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "Service Orders", description = "Endpoints for managing the Service Order (SO) lifecycle")
public interface ServiceOrderOpenApi {

    @Operation(summary = "Opens a new Service Order")
    ResponseEntity<ServiceOrderResponse> create(ServiceOrderRequest serviceOrderRequest);

    @Operation(summary = "Updates data for an open Service Order")
    ResponseEntity<ServiceOrderResponse> update(
            @Parameter(description = "Service Order ID") UUID id,
            UpdateServiceOrderRequest updateServiceOrderRequest);

    @Operation(summary = "Retrieves a Service Order by ID")
    ResponseEntity<ServiceOrderResponse> findById(@Parameter(description = "Service Order ID") UUID id);

    @Operation(summary = "Retrieves a paginated list of Service Orders with detailed filters")
    ResponseEntity<Page<ServiceOrderResponse>> findAll(
            String code, UUID customerId, String serviceTitle, ServiceOrderStatus serviceOrderStatus,
            BigDecimal totalValueMin, BigDecimal totalValueMax, Boolean hasPayment,
            LocalDateTime createdAtFrom, LocalDateTime createdAtTo, UUID assignedEmployeeId,
            SortBy sortBy, Integer page, Integer size);

    @Operation(summary = "Deletes a Service Order")
    ResponseEntity<Void> delete(@Parameter(description = "Service Order ID") UUID id);

    @Operation(summary = "State Transition: Starts the Service Order processing")
    ResponseEntity<ServiceOrderResponse> startProcess(@Parameter(description = "Service Order ID") UUID id);

    @Operation(summary = "State Transition: Finishes the Service Order processing")
    ResponseEntity<ServiceOrderResponse> finishProcess(@Parameter(description = "Service Order ID") UUID id);

    @Operation(summary = "State Transition: Delivers the equipment/service to the customer")
    ResponseEntity<ServiceOrderResponse> deliverProcess(@Parameter(description = "Service Order ID") UUID id);

    @Operation(summary = "State Transition: Cancels the Service Order")
    ResponseEntity<ServiceOrderResponse> cancelProcess(@Parameter(description = "Service Order ID") UUID id);
}