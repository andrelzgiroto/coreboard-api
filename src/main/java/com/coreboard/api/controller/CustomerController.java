package com.coreboard.api.controller;

import com.coreboard.api.controller.openapi.CustomerOpenApi;
import com.coreboard.api.domain.dto.customer.CustomerRequest;
import com.coreboard.api.domain.dto.customer.CustomerResponse;
import com.coreboard.api.domain.dto.customer.UpdateCustomerRequest;
import com.coreboard.api.domain.enums.SortBy;
import com.coreboard.api.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController implements CustomerOpenApi {

    private final CustomerService customerService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest customerRequest) {
        CustomerResponse response = customerService.create(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateCustomerRequest updateCustomerRequest) {

        CustomerResponse response = customerService.update(id, updateCustomerRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<CustomerResponse> findById(@PathVariable("id") UUID id) {
        CustomerResponse response = customerService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<Page<CustomerResponse>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) LocalDateTime createdAtFrom,
            @RequestParam(required = false) LocalDateTime createdAtTo,
            @RequestParam(required = false, defaultValue = "MOST_RECENT") SortBy sortBy,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "18") Integer size) {

        Page<CustomerResponse> response = customerService.findAll(
                name,
                phone,
                email,
                cpf,
                createdAtFrom,
                createdAtTo,
                sortBy,
                page,
                size);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
