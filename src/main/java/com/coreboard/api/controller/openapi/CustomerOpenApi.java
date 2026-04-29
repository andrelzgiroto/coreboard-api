package com.coreboard.api.controller.openapi;

import com.coreboard.api.domain.dto.customer.CustomerRequest;
import com.coreboard.api.domain.dto.customer.CustomerResponse;
import com.coreboard.api.domain.dto.customer.UpdateCustomerRequest;
import com.coreboard.api.domain.enums.SortBy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "Customers", description = "Endpoints for customer management")
public interface CustomerOpenApi {

    @Operation(summary = "Registers a new customer")
    ResponseEntity<CustomerResponse> create(CustomerRequest customerRequest);

    @Operation(summary = "Updates an existing customer's data")
    ResponseEntity<CustomerResponse> update(
            @Parameter(description = "Customer ID") UUID id,
            UpdateCustomerRequest updateCustomerRequest);

    @Operation(summary = "Retrieves a customer by ID")
    ResponseEntity<CustomerResponse> findById(@Parameter(description = "Customer ID") UUID id);

    @Operation(summary = "Retrieves a paginated list of customers with optional filters")
    ResponseEntity<Page<CustomerResponse>> findAll(
            String name, String phone, String email, String cpf,
            LocalDateTime createdAtFrom, LocalDateTime createdAtTo,
            SortBy sortBy, Integer page, Integer size);

    @Operation(summary = "Deletes a customer from the system")
    ResponseEntity<Void> delete(@Parameter(description = "Customer ID") UUID id);
}