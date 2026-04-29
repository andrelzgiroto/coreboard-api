package com.coreboard.api.controller;

import com.coreboard.api.controller.openapi.EmployeeOpenApi;
import com.coreboard.api.domain.dto.employee.EmployeeRequest;
import com.coreboard.api.domain.dto.employee.EmployeeResponse;
import com.coreboard.api.domain.dto.employee.UpdateEmployeeRequest;
import com.coreboard.api.domain.enums.SortBy;
import com.coreboard.api.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class EmployeeController implements EmployeeOpenApi {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest employeeRequest) {
        EmployeeResponse employeeResponse = employeeService.create(employeeRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(employeeResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {

        return ResponseEntity.ok(employeeService.update(id, updateEmployeeRequest));
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> findAll(
            @RequestParam(required = false, defaultValue = "MOST_RECENT") SortBy sortBy,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "18") Integer size
    ) {
        Page<EmployeeResponse> response = employeeService.findAll(
                sortBy,
                page,
                size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> findById(@PathVariable("id") UUID id) {
        EmployeeResponse response = employeeService.findById(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeResponse> delete(@PathVariable("id") UUID id) {
        employeeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
