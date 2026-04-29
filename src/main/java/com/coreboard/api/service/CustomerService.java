package com.coreboard.api.service;

import com.coreboard.api.domain.dto.customer.CustomerRequest;
import com.coreboard.api.domain.dto.customer.CustomerResponse;
import com.coreboard.api.domain.dto.customer.UpdateCustomerRequest;
import com.coreboard.api.domain.entity.Customer;
import com.coreboard.api.domain.enums.SortBy;
import com.coreboard.api.exception.BusinessException;
import com.coreboard.api.exception.ResourceNotFoundException;
import com.coreboard.api.mapper.CustomerMapper;
import com.coreboard.api.repository.CustomerRepository;
import com.coreboard.api.repository.specification.CustomerSpecification;
import com.coreboard.api.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    public CustomerResponse create(CustomerRequest customerRequest) {
        if (customerRepository.existsByCpf(customerRequest.cpf())) {
            throw new BusinessException("Customer has already been registered.");
        }

        Customer customer = customerMapper.toEntity(customerRequest);
        customerRepository.save(customer);

        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse update(UUID id, UpdateCustomerRequest updateCustomerRequest) {
        Customer customer = findEntityById(id);
        customerMapper.updateEntity(updateCustomerRequest, customer);

        return customerMapper.toResponse(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID id) {
        return customerRepository
                .findById(id)
                .map(customerMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not founded"));
    }

    @Transactional(readOnly = true)
    public Customer findEntityById(UUID id) {
        return customerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not founded"));
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponse> findAll(
            String name,
            String phone,
            String email,
            String cpf,
            LocalDateTime createdAtFrom,
            LocalDateTime createdAtTo,
            SortBy sortBy,
            Integer page,
            Integer size) {

        Specification<Customer> spec = Specification.allOf(
                CustomerSpecification.byNameLike(name),
                CustomerSpecification.byPhoneLike(phone),
                CustomerSpecification.byEmailExact(email),
                CustomerSpecification.byCpfExact(cpf),
                CustomerSpecification.createdAtFrom(createdAtFrom),
                CustomerSpecification.createdAtTo(createdAtTo)
        );

        Pageable pageable = PageableUtils.createWithSort(page, size, sortBy, null);

        return customerRepository
                .findAll(spec, pageable)
                .map(customerMapper::toResponse);
    }

    public void delete(UUID id) {
        Customer customer = findEntityById(id);
        customerRepository.delete(customer);
    }
}
