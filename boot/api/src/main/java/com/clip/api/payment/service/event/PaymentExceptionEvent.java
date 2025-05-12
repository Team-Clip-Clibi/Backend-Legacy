package com.clip.api.payment.service.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentExceptionEvent {
    private final String paymentKey;
    @Builder
    public PaymentExceptionEvent(String paymentKey) {
        this.paymentKey = paymentKey;
    }
}
