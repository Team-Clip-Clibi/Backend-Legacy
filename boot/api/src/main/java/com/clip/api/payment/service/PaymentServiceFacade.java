package com.clip.api.payment.service;

import com.clip.api.payment.controller.dto.PaymentDto;
import com.clip.api.payment.feign.TossPaymentFeign;
import com.clip.api.payment.feign.dto.PaymentConfirmDto;
import com.clip.api.payment.feign.dto.PaymentObject;
import com.clip.order.entity.OneThingOrder;
import com.clip.order.entity.RandomOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceFacade {

    private final TossPaymentFeign tossPaymentFeign;
    private final UserPaymentService userPaymentService;

    public void confirm(long userId, PaymentDto paymentDto) {
        PaymentObject paymentObject;
        switch (paymentDto.getOrderType()) {
            case RANDOM -> {
                RandomOrder randomOrder = userPaymentService.findRandomOrder(userId, paymentDto.getOrderId());
                paymentObject = tossPaymentFeign.confirmPayment(PaymentConfirmDto.builder()
                        .paymentKey(paymentDto.getPaymentKey())
                        .orderId(paymentDto.getOrderId())
                        .amount(randomOrder.getAmount())
                        .build()
                );
                userPaymentService.updateRandomOrderStatus(userId, paymentObject);
            }
            case ONETHING -> {
                OneThingOrder oneThingOrder = userPaymentService.findOneThingOrder(userId, paymentDto.getOrderId());
                paymentObject = tossPaymentFeign.confirmPayment(PaymentConfirmDto.builder()
                        .paymentKey(paymentDto.getPaymentKey())
                        .orderId(paymentDto.getOrderId())
                        .amount(oneThingOrder.getAmount())
                        .build()
                );
                userPaymentService.updateOneThingOrderStatus(userId, paymentObject);
            }
        }
    }

}