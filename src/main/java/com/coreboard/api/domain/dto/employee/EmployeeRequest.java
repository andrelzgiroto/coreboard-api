package com.coreboard.api.domain.dto.employee;

import com.coreboard.api.domain.enums.EmployeeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload for adding a new employee/operator")
public record EmployeeRequest(

        @Schema(description = "Employee's full name", example = "Charles Technician")
        @NotBlank(message = "Name is required.")
        @Size(max = 150)
        String name,

        @Schema(description = "Corporate email for access", example = "charles@coreboard.com")
        @NotBlank(message = "Email is required.")
        @Size(max = 100)
        @Email
        String email,

        @Schema(description = "Initial access password", example = "SecurePassword123")
        @NotBlank(message = "Password is required.")
        @Size(min = 8, max = 30)
        String password,

        @Schema(description = "Permission level/role in the system", example = "OPERATOR")
        @NotBlank(message = "Employee type is required.")
        EmployeeType employeeType
) {}
