package com.clip.office.payment.service;

import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.matching.service.UserOneThingMatchingService;
import com.clip.matching.service.UserRandomMatchingService;
import com.clip.office.payment.feign.dto.PaymentObject;
import com.clip.office.payment.mapper.TossPaymentMapper;
import com.clip.order.entity.OneThingOrder;
import com.clip.order.entity.OneThingOrderStatus;
import com.clip.order.entity.RandomOrder;
import com.clip.order.entity.RandomOrderStatus;
import com.clip.order.service.OneThingOrderService;
import com.clip.order.service.RandomOrderService;
import com.clip.toss.TossPaymentService;
import com.clip.toss.entity.TossPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminPaymentService {

    private final UserOneThingMatchingService userOneThingMatchingService;
    private final OneThingOrderService oneThingOrderService;
    private final TossPaymentService tossPaymentService;
    private final TossPaymentMapper tossPaymentMapper;
    private final UserRandomMatchingService userRandomMatchingService;
    private final RandomOrderService randomOrderService;

    @Transactional
    public List<UserOneThingMatching> updateOnethingMatchingStatusToMatchingFail(long onethingMatchingId) {
        return userOneThingMatchingService.updateOnethingMatchingStatusToMatchingFail(onethingMatchingId);
    }

    public List<UserRandomMatching> updateRandomMatchingStatusToMatchingFail(long randomMatchingId) {
        return userOneThingMatchingService.updateRandomMatchingStatusToMatchingFail(randomMatchingId);
    }

    @Transactional
    public void updateAllOnethingOrdersStatusAndTossPayment(Long onethingMatchingId, List<PaymentObject> paymentObjects) {
        Map<UUID, TossPayment> orderIdToTossPayment = paymentObjects.stream()
                .collect(Collectors.toMap(
                        PaymentObject::getOrderId,
                        tossPaymentMapper::toTossPayment)
                );

        tossPaymentService.saveAll(new ArrayList<>(orderIdToTossPayment.values()));
        List<OneThingOrder> oneThingOrders = userOneThingMatchingService.updateOrderStatusToCancel(onethingMatchingId).stream()
                .map(onethingOrder -> {
                    if (!Objects.isNull(orderIdToTossPayment.get(onethingOrder.getOrderId()))) {
                        onethingOrder.addTossPayment(orderIdToTossPayment.get(onethingOrder.getOrderId()));
                        onethingOrder.updateStatus(OneThingOrderStatus.CANCELED);
                    }
                    return onethingOrder;
                })
                .toList();

        oneThingOrderService.saveAll(oneThingOrders);
    }

    public void updateAllRandomOrdersStatusAndTossPayment(Long randomMatchingId, List<PaymentObject> paymentObjects) {
        Map<UUID, TossPayment> orderIdToTossPayment = paymentObjects.stream()
                .collect(Collectors.toMap(
                        PaymentObject::getOrderId,
                        tossPaymentMapper::toTossPayment)
                );

        tossPaymentService.saveAll(new ArrayList<>(orderIdToTossPayment.values()));
        List<RandomOrder> randomOrders = userRandomMatchingService.updateOrderStatusToCancel(randomMatchingId).stream()
                .map(randomOrder -> {
                    if (!Objects.isNull(orderIdToTossPayment.get(randomOrder.getOrderId()))) {
                        randomOrder.addTossPayment(orderIdToTossPayment.get(randomOrder.getOrderId()));
                        randomOrder.updateStatus(RandomOrderStatus.CANCELED);
                    }
                    return randomOrder;
                })
                .toList();
        randomOrderService.saveAll(randomOrders);
    }
}
