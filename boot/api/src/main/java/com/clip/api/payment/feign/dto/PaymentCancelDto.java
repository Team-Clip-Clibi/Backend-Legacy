package com.clip.api.payment.feign.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentCancelDto {
    private final String cancelReason;

    @Builder
    public PaymentCancelDto(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}