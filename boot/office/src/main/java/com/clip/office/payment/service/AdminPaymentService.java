package com.clip.office.payment.service;

import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.service.UserOneThingMatchingService;
import com.clip.office.payment.feign.dto.PaymentObject;
import com.clip.office.payment.mapper.TossPaymentMapper;
import com.clip.order.entity.OneThingOrder;
import com.clip.order.entity.OneThingOrderStatus;
import com.clip.order.service.OneThingOrderService;
import com.clip.toss.TossPaymentService;
import com.clip.toss.entity.TossPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminPaymentService {

    private final UserOneThingMatchingService userOneThingMatchingService;
    private final OneThingOrderService oneThingOrderService;
    private final TossPaymentService tossPaymentService;
    private final TossPaymentMapper tossPaymentMapper;

    @Transactional
    public List<UserOneThingMatching> updateMatchingStatusToMatchingFail(long onethingMatchingId) {
        return userOneThingMatchingService.updateMatchingStatusToMatchingFail(onethingMatchingId);
    }

    @Transactional
    public void updateAllOrdersStatusAndTossPayment(Long onethingMatchingId, List<PaymentObject> paymentObjects) {
        Map<UUID, TossPayment> orderIdToTossPayment = paymentObjects.stream()
                .collect(Collectors.toMap(
                        PaymentObject::getOrderId,
                        tossPaymentMapper::toTossPayment)
                );

        tossPaymentService.saveAll(new ArrayList<>(orderIdToTossPayment.values()));
        List<OneThingOrder> oneThingOrders = userOneThingMatchingService.updateOrderStatusToCancel(onethingMatchingId).stream()
                .map(onethingOrder -> {
                    onethingOrder.addTossPayment(orderIdToTossPayment.get(onethingOrder.getOrderId()));
                    onethingOrder.updateStatus(OneThingOrderStatus.CANCELED);
                    return onethingOrder;
                })
                .toList();

        oneThingOrderService.saveAll(oneThingOrders);
    }
}
