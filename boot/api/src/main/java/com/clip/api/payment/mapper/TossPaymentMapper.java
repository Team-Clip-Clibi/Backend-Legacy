package com.clip.api.payment.mapper;

import com.clip.api.payment.feign.dto.PaymentObject;
import com.clip.toss.entity.TossPayment;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TossPaymentMapper {

    @Mappings({
            @Mapping(source = "paymentKey", target = "paymentId"),
            @Mapping(source = "totalAmount", target = "amount"),
            @Mapping(source = "receipt.url", target = "receipt_url"),
    })
    TossPayment toTossPayment(PaymentObject paymentObject);
}
