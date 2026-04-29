package com.coreboard.api.mapper;

import com.coreboard.api.domain.dto.paymentreceipt.PaymentReceiptRequest;
import com.coreboard.api.domain.dto.paymentreceipt.PaymentReceiptResponse;
import com.coreboard.api.domain.entity.PaymentReceipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentReceiptMapper {

    @Mapping(target = "serviceOrder", ignore = true)
    PaymentReceipt toEntity(PaymentReceiptRequest paymentReceiptRequest);

    @Mapping(source = "serviceOrder.id", target = "serviceOrderId")
    PaymentReceiptResponse toResponse(PaymentReceipt paymentReceipt);
}
