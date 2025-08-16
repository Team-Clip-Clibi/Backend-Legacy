package com.clip.office.payment.mapper;

import com.clip.office.payment.feign.dto.PaymentObject;
import com.clip.toss.entity.TossPayment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TossPaymentMapper {

    @Mappings({
            @Mapping(source = "paymentKey", target = "paymentId"),
            @Mapping(source = "totalAmount", target = "amount"),
            @Mapping(source = "receipt.url", target = "receipt_url"),
            @Mapping(source = ".", target = "jsonResponsePayload", qualifiedByName = "paymentObjectToJson")
    })
    TossPayment toTossPayment(PaymentObject paymentObject);

    @Named("paymentObjectToJson")
    default String paymentObjectToJson(PaymentObject obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }
    }
}
