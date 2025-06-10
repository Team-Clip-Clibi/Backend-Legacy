package com.clip.api.payment.service;

import com.clip.api.payment.controller.dto.PaymentDto;
import com.clip.api.payment.feign.TossPaymentFeign;
import com.clip.api.payment.feign.dto.PaymentConfirmDto;
import com.clip.api.payment.feign.dto.PaymentObject;
import com.clip.order.entity.OneThingOrder;
import com.clip.order.entity.RandomOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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
                        .amount(randomOrder.getDiscountedPrice())
                        .build()
                );
                log.info("paymentObject info: {}", paymentObject.toString());
                userPaymentService.updateRandomOrderStatus(userId, paymentObject);
            }
            case ONETHING -> {
                OneThingOrder oneThingOrder = userPaymentService.findOneThingOrder(userId, paymentDto.getOrderId());
                paymentObject = tossPaymentFeign.confirmPayment(PaymentConfirmDto.builder()
                        .paymentKey(paymentDto.getPaymentKey())
                        .orderId(paymentDto.getOrderId())
                        .amount(oneThingOrder.getDiscountedPrice())
                        .build()
                );
                log.info("paymentObject info: {}", paymentObject.toString());
                userPaymentService.updateOneThingOrderStatus(userId, paymentObject);
            }
        }
    }

}