package com.coreboard.api.mapper;

import com.coreboard.api.domain.dto.customer.CustomerRequest;
import com.coreboard.api.domain.dto.customer.CustomerResponse;
import com.coreboard.api.domain.dto.customer.UpdateCustomerRequest;
import com.coreboard.api.domain.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toEntity(CustomerRequest customerRequest);

    CustomerResponse toResponse(Customer customer);

    void updateEntity(UpdateCustomerRequest updateCustomerRequest, @MappingTarget Customer customer);
}
