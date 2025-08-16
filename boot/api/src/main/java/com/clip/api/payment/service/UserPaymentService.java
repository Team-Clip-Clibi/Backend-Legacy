package com.clip.api.payment.service;

import com.clip.api.payment.feign.dto.PaymentObject;
import com.clip.api.payment.mapper.TossPaymentMapper;
import com.clip.api.payment.service.event.PaymentExceptionEvent;
import com.clip.matching.entity.RandomMatchingStatus;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.matching.service.UserRandomMatchingService;
import com.clip.order.entity.OneThingOrder;
import com.clip.order.entity.OneThingOrderStatus;
import com.clip.order.entity.RandomOrder;
import com.clip.order.entity.RandomOrderStatus;
import com.clip.order.service.OneThingOrderService;
import com.clip.order.service.RandomOrderService;
import com.clip.toss.TossPaymentService;
import com.clip.toss.entity.TossPayment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPaymentService {

    private final TossPaymentMapper tossPaymentMapper;

    private final OneThingOrderService oneThingOrderService;
    private final RandomOrderService randomOrderService;
    private final UserRandomMatchingService userRandomMatchingService;
    private final TossPaymentService tossPaymentService;
    private final ApplicationEventPublisher eventPublisher;

    public OneThingOrder findOneThingOrder(long userId, UUID orderId) {
        return oneThingOrderService.findOneThingOrder(userId, orderId);
    }

    public RandomOrder findRandomOrder(long userId, UUID orderId) {
        return randomOrderService.findRandomOrder(userId, orderId);
    }

    @Transactional
    public void updateOneThingOrderStatus(long userId, PaymentObject paymentObject) {
        eventPublisher.publishEvent(PaymentExceptionEvent.builder()
                .paymentKey(paymentObject.getPaymentKey())
                .build()
        );

        OneThingOrder oneThingOrder = oneThingOrderService.findOneThingOrder(userId, paymentObject.getOrderId());
        TossPayment tossPayment = tossPaymentMapper.toTossPayment(paymentObject);
        oneThingOrder.updateStatus(OneThingOrderStatus.DONE);
        oneThingOrder.addTossPayment(tossPayment);
    }

    @Transactional
    public void updateRandomStatus(long userId, PaymentObject paymentObject) {
        eventPublisher.publishEvent(PaymentExceptionEvent.builder()
                .paymentKey(paymentObject.getPaymentKey())
                .build()
        );

        RandomOrder randomOrder = randomOrderService.findRandomOrder(userId, paymentObject.getOrderId());
        TossPayment tossPayment = tossPaymentMapper.toTossPayment(paymentObject);
        tossPaymentService.save(tossPayment);
        randomOrder.updateStatus(RandomOrderStatus.DONE);
        randomOrder.addTossPayment(tossPayment);

        UserRandomMatching userRandomMatching = userRandomMatchingService.findUserRandomMatching(userId, randomOrder.getId());
        userRandomMatching.updateStatus(RandomMatchingStatus.CONFIRMED);
    }
}
