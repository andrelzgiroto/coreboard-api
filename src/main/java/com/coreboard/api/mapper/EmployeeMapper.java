package com.coreboard.api.mapper;

import com.coreboard.api.domain.dto.employee.EmployeeRequest;
import com.coreboard.api.domain.dto.employee.EmployeeResponse;
import com.coreboard.api.domain.dto.employee.UpdateEmployeeRequest;
import com.coreboard.api.domain.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee toEntity(EmployeeRequest employeeRequest);

    EmployeeResponse toResponse(Employee employee);

    void updateEntity(UpdateEmployeeRequest updateEmployeeRequest, @MappingTarget Employee employee);


}
