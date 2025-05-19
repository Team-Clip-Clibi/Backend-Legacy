package com.clip.api.payment.feign.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class PaymentConfirmDto {
    private final String paymentKey;
    private final UUID orderId;
    private final BigDecimal amount;

    @Builder
    public PaymentConfirmDto(String paymentKey, UUID orderId, BigDecimal amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }
}
