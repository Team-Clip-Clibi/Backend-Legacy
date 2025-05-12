package com.clip.api.payment.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class PaymentDto {
    private String paymentKey;
    private UUID orderId;
    private OrderType orderType;

    @Builder
    public PaymentDto(String paymentKey, UUID orderId, OrderType orderType) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderType = orderType;
    }
}
