package com.coreboard.api.controller.openapi;

import com.coreboard.api.domain.dto.employee.EmployeeRequest;
import com.coreboard.api.domain.dto.employee.EmployeeResponse;
import com.coreboard.api.domain.dto.employee.UpdateEmployeeRequest;
import com.coreboard.api.domain.enums.SortBy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Employees", description = "Endpoints for employee and operator management")
public interface EmployeeOpenApi {

    @Operation(summary = "Registers a new employee")
    ResponseEntity<EmployeeResponse> create(EmployeeRequest employeeRequest);

    @Operation(summary = "Updates an existing employee's data")
    ResponseEntity<EmployeeResponse> update(
            @Parameter(description = "Employee ID") UUID id,
            UpdateEmployeeRequest updateEmployeeRequest);

    @Operation(summary = "Retrieves a paginated list of employees")
    ResponseEntity<Page<EmployeeResponse>> findAll(SortBy sortBy, Integer page, Integer size);

    @Operation(summary = "Retrieves an employee by ID")
    ResponseEntity<EmployeeResponse> findById(@Parameter(description = "Employee ID") UUID id);

    @Operation(summary = "Deletes an employee from the system")
    ResponseEntity<EmployeeResponse> delete(@Parameter(description = "Employee ID") UUID id);
}