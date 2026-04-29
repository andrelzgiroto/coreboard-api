package com.coreboard.api.service;

import com.coreboard.api.domain.dto.serviceorder.ServiceOrderRequest;
import com.coreboard.api.domain.dto.serviceorder.ServiceOrderResponse;
import com.coreboard.api.domain.dto.serviceorder.UpdateServiceOrderRequest;
import com.coreboard.api.domain.entity.Customer;
import com.coreboard.api.domain.entity.Employee;
import com.coreboard.api.domain.entity.ServiceOrder;
import com.coreboard.api.domain.enums.ServiceOrderStatus;
import com.coreboard.api.domain.enums.SortBy;
import com.coreboard.api.exception.BusinessException;
import com.coreboard.api.exception.ResourceNotFoundException;
import com.coreboard.api.mapper.ServiceOrderMapper;
import com.coreboard.api.repository.ServiceOrderRepository;
import com.coreboard.api.repository.specification.ServiceOrderSpecification;
import com.coreboard.api.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderMapper serviceOrderMapper;
    private final CustomerService customerService;
    private final EmployeeService employeeService;

    @Transactional
    public ServiceOrderResponse create(ServiceOrderRequest serviceOrderRequest) {
        ServiceOrder serviceOrder = serviceOrderMapper.toEntity(serviceOrderRequest);

        Customer customer = customerService.findEntityById(serviceOrderRequest.customerId());
        Employee employee = employeeService.findEntityById(serviceOrderRequest.assignedEmployeeId());
        serviceOrder.setCustomer(customer);
        serviceOrder.setAssignedEmployee(employee);

        serviceOrder.setServiceOrderStatus(ServiceOrderStatus.OPEN);
        serviceOrder.setCode(generateServiceOrderCode());
        serviceOrderRepository.save(serviceOrder);

        return serviceOrderMapper.toResponse(serviceOrder);
    }

    @Transactional
    public ServiceOrderResponse update(UUID id, UpdateServiceOrderRequest updateServiceOrderRequest) {
        ServiceOrder serviceOrder = findEntityById(id);

        if (serviceOrder.getServiceOrderStatus() != ServiceOrderStatus.OPEN) {
            throw new BusinessException("The service can only be updated while it is OPEN.");
        }

        serviceOrderMapper.updateEntity(updateServiceOrderRequest, serviceOrder);

        return serviceOrderMapper.toResponse(serviceOrder);
    }

    @Transactional(readOnly = true)
    public ServiceOrderResponse findById(UUID id) {
        return serviceOrderRepository
                .findById(id)
                .map(serviceOrderMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Service Order not found"));
    }

    @Transactional(readOnly = true)
    public ServiceOrder findEntityById(UUID id) {
        return serviceOrderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service Order not found"));
    }

    @Transactional(readOnly = true)
    public Page<ServiceOrderResponse> findAll(
            String code,
            UUID customerId,
            String serviceTitle,
            ServiceOrderStatus serviceOrderStatus,
            BigDecimal totalValueMin,
            BigDecimal totalValueMax,
            Boolean hasPayment,
            LocalDateTime createdAtFrom,
            LocalDateTime createdAtTo,
            UUID assignedEmployeeId,
            SortBy sortBy,
            Integer page,
            Integer size) {

        Specification<ServiceOrder> spec = Specification.allOf(
                ServiceOrderSpecification.byCodeStartsWith(code),
                ServiceOrderSpecification.byCustomerId(customerId),
                ServiceOrderSpecification.byServiceTitleLike(serviceTitle),
                ServiceOrderSpecification.byServiceOrderStatus(serviceOrderStatus),
                ServiceOrderSpecification.bytotalValueMin(totalValueMin),
                ServiceOrderSpecification.bytotalValueMax(totalValueMax),
                ServiceOrderSpecification.hasPayment(hasPayment),
                ServiceOrderSpecification.createdAtFrom(createdAtFrom),
                ServiceOrderSpecification.createdAtTo(createdAtTo),
                ServiceOrderSpecification.byAssignedEmployeeId(assignedEmployeeId)
        );

        Pageable pageable = PageableUtils.createWithSort(page, size, sortBy, "totalValue");

        return serviceOrderRepository
                .findAll(spec, pageable)
                .map(serviceOrderMapper::toResponse);
    }

    @Transactional
    public void delete(UUID id) {
        ServiceOrder serviceOrder = findEntityById(id);
        serviceOrderRepository.delete(serviceOrder);
    }

    public boolean existsByIdAndPaymentReceipt(UUID id) {
        return serviceOrderRepository.existsByIdAndPaymentReceiptNotNull(id);
    }

    @Transactional
    public ServiceOrderResponse startProcess(UUID id) {
        ServiceOrder serviceOrder = findEntityById(id);
        serviceOrder.start();

        return serviceOrderMapper.toResponse(serviceOrder);
    }

    @Transactional
    public ServiceOrderResponse finishProcess(UUID id) {
        ServiceOrder serviceOrder = findEntityById(id);
        serviceOrder.finish();

        return serviceOrderMapper.toResponse(serviceOrder);
    }

    @Transactional
    public ServiceOrderResponse deliverProcess(UUID id) {
        ServiceOrder serviceOrder = findEntityById(id);
        serviceOrder.deliver();

        return serviceOrderMapper.toResponse(serviceOrder);
    }

    @Transactional
    public ServiceOrderResponse cancelProcess(UUID id) {
        ServiceOrder serviceOrder = findEntityById(id);
        serviceOrder.cancel();

        return serviceOrderMapper.toResponse(serviceOrder);
    }

    private String generateServiceOrderCode() {
        Long nextNumber = serviceOrderRepository.getNextCodeSequence();
        return "SO-%04d".formatted(nextNumber);
    }
}
