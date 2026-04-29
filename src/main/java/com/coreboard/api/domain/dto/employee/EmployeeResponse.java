package com.coreboard.api.domain.dto.employee;

import com.coreboard.api.domain.enums.EmployeeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Schema(description = "Representation of employee data without sensitive information")
public record EmployeeResponse(
        @Schema(description = "Unique employee ID")
        UUID id,

        @Schema(description = "Employee's name", example = "Charles Technician")
        String name,

        @Schema(description = "Access email", example = "charles@coreboard.com")
        String email,

        @Schema(description = "Access profile", example = "OPERATOR")
        EmployeeType employeeType,

        @Schema(description = "Creation/hiring date")
        LocalDateTime createdAt,

        @Schema(description = "Date of the last profile modification")
        LocalDateTime updatedAt
) {}