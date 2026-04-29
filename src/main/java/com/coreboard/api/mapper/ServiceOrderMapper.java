package com.coreboard.api.mapper;

import com.coreboard.api.domain.dto.serviceorder.ServiceOrderRequest;
import com.coreboard.api.domain.dto.serviceorder.ServiceOrderResponse;
import com.coreboard.api.domain.dto.serviceorder.UpdateServiceOrderRequest;
import com.coreboard.api.domain.entity.ServiceOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ServiceOrderMapper {

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "assignedEmployee", ignore = true)
    ServiceOrder toEntity(ServiceOrderRequest serviceOrderRequest);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "paymentReceipt.id", target = "paymentReceiptId")
    @Mapping(source = "assignedEmployee.id", target = "assignedEmployeeId")
    ServiceOrderResponse toResponse(ServiceOrder serviceOrder);

    void updateEntity(UpdateServiceOrderRequest updateServiceOrderRequest, @MappingTarget ServiceOrder serviceOrder);

}
