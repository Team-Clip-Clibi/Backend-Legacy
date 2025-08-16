package com.clip.office.payment.feign.dto;

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