package com.coreboard.api.service;

import com.coreboard.api.domain.dto.employee.EmployeeRequest;
import com.coreboard.api.domain.dto.employee.EmployeeResponse;
import com.coreboard.api.domain.dto.employee.UpdateEmployeeRequest;
import com.coreboard.api.domain.entity.Employee;
import com.coreboard.api.domain.enums.SortBy;
import com.coreboard.api.exception.BusinessException;
import com.coreboard.api.util.PageableUtils;
import com.coreboard.api.exception.ResourceNotFoundException;
import com.coreboard.api.mapper.EmployeeMapper;
import com.coreboard.api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder encoder;

    @Transactional
    public EmployeeResponse create(EmployeeRequest employeeRequest) {
        if (employeeRepository.existsByEmail(employeeRequest.email())) {
            throw new BusinessException("Employee is already registered.");
        }

        Employee employee = employeeMapper.toEntity(employeeRequest);

        employee.setPassword(encoder.encode(employeeRequest.password()));
        Employee employeeSaved = employeeRepository.save(employee);

        return employeeMapper.toResponse(employeeSaved);
    }

    @Transactional
    public EmployeeResponse update(UUID id, UpdateEmployeeRequest updateEmployeeRequest) {
        Employee employee = findEntityById(id);
        employeeMapper.updateEntity(updateEmployeeRequest, employee);

        return employeeMapper.toResponse(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeResponse findById(UUID id) {
        return employeeRepository
                .findById(id)
                .map(employeeMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    @Transactional(readOnly = true)
    public Employee findEntityById(UUID id) {
        return employeeRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    @Transactional(readOnly = true)
    public Employee findEntityByEmail(String email) {
        return employeeRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponse> findAll(
            SortBy sortBy,
            Integer page,
            Integer size) {

        Pageable pageable = PageableUtils.createWithSort(page, size, sortBy, null);
        return employeeRepository
                .findAll(pageable)
                .map(employeeMapper::toResponse);
    }

    public void delete(UUID id) {
        Employee employee = findEntityById(id);
        employeeRepository.delete(employee);
    }

}
