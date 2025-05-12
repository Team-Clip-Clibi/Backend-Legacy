package com.clip.api.payment.feign.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PaymentConfirmDto {
    private final String paymentKey;
    private final UUID orderId;
    private final int amount;

    @Builder
    public PaymentConfirmDto(String paymentKey, UUID orderId, int amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }
}
