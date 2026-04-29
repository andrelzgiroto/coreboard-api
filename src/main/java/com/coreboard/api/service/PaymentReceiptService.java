package com.coreboard.api.service;

import com.coreboard.api.domain.dto.paymentreceipt.PaymentReceiptRequest;
import com.coreboard.api.domain.dto.paymentreceipt.PaymentReceiptResponse;
import com.coreboard.api.domain.entity.PaymentReceipt;
import com.coreboard.api.domain.entity.ServiceOrder;
import com.coreboard.api.domain.enums.PaymentMethod;
import com.coreboard.api.domain.enums.ServiceOrderStatus;
import com.coreboard.api.domain.enums.SortBy;
import com.coreboard.api.exception.BusinessException;
import com.coreboard.api.exception.ResourceNotFoundException;
import com.coreboard.api.mapper.PaymentReceiptMapper;
import com.coreboard.api.repository.PaymentReceiptRepository;
import com.coreboard.api.repository.specification.PaymentReceiptSpecification;
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
public class PaymentReceiptService {

    private final PaymentReceiptRepository paymentReceiptRepository;
    private final PaymentReceiptMapper paymentReceiptMapper;
    private final ServiceOrderService serviceOrderService;

    @Transactional
    public PaymentReceiptResponse registerPayment(PaymentReceiptRequest paymentReceiptRequest) {
        PaymentReceipt paymentReceipt = paymentReceiptMapper.toEntity(paymentReceiptRequest);
        ServiceOrder serviceOrder = serviceOrderService.findEntityById(paymentReceiptRequest.serviceOrderId());

        if (serviceOrderService.existsByIdAndPaymentReceipt(serviceOrder.getId())) {
            throw new BusinessException("Payment for this order has already been made.");
        }

        if (serviceOrder.getServiceOrderStatus() != ServiceOrderStatus.FINISHED) {
            throw new BusinessException("Service order must be completed before payment can be processed.");
        }

        if (!serviceOrder.getTotalValue().equals(paymentReceiptRequest.amount())) {
            throw new BusinessException("Payment must correspond to the exact cost of the service.");
        }

        paymentReceipt.setServiceOrder(serviceOrder);
        serviceOrder.setPaymentReceipt(paymentReceipt);

        paymentReceiptRepository.save(paymentReceipt);

        return paymentReceiptMapper.toResponse(paymentReceipt);
    }

    @Transactional(readOnly = true)
    public PaymentReceiptResponse findById(UUID id) {
        return paymentReceiptRepository
                .findById(id)
                .map(paymentReceiptMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Receipt not found"));
    }

    public PaymentReceipt findEntityById(UUID id) {
        return paymentReceiptRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Receipt not found"));
    }

    @Transactional(readOnly = true)
    public Page<PaymentReceiptResponse> findAll(
            UUID byServiceOrderId,
            BigDecimal amountMin,
            BigDecimal amountMax,
            PaymentMethod paymentMethod,
            LocalDateTime receivedAtFrom,
            LocalDateTime receivedAtTo,
            SortBy sort,
            Integer page,
            Integer size) {

        Specification<PaymentReceipt> spec = Specification.allOf(
                PaymentReceiptSpecification.byServiceOrderId(byServiceOrderId),
                PaymentReceiptSpecification.byAmountMin(amountMin),
                PaymentReceiptSpecification.byAmountMax(amountMax),
                PaymentReceiptSpecification.byPaymentMethod(paymentMethod),
                PaymentReceiptSpecification.receivedAtFrom(receivedAtFrom),
                PaymentReceiptSpecification.receivedAtTo(receivedAtTo)
        );

        Pageable pageable = PageableUtils.createWithSort(page, size, sort, "amount");

        return paymentReceiptRepository
                .findAll(spec, pageable)
                .map(paymentReceiptMapper::toResponse);
    }
}
