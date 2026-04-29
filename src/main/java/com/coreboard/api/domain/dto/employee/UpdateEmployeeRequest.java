package com.coreboard.api.domain.dto.employee;

import com.coreboard.api.domain.enums.EmployeeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload for updating employee base data and profile")
public record UpdateEmployeeRequest(

        @Schema(description = "Updated employee name", example = "Charles Edward Technician")
        @NotBlank(message = "Name is required.")
        @Size(max = 150)
        String name,

        @Schema(description = "New access profile", example = "ADMIN")
        @NotBlank(message = "Employee type is required.")
        EmployeeType employeeType
) {}