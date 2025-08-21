package com.clip.office.payment.service;

import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.office.payment.feign.TossPaymentFeign;
import com.clip.office.payment.feign.dto.PaymentCancelDto;
import com.clip.office.payment.feign.dto.PaymentObject;
import com.clip.toss.entity.TossPayment;
import com.clip.toss.entity.TossPaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPaymentFacade {
    private final String CANCEL_REASON = "매칭 실패 결제 취소";
    private final TossPaymentFeign tossPaymentFeign;
    private final AdminPaymentService adminPaymentService;

    public void cancelAllOnethingOrder(Long onethingMatchingId) {
        List<UserOneThingMatching> usrOnethingMatchings = adminPaymentService.updateOnethingMatchingStatusToMatchingFail(onethingMatchingId);
        List<PaymentObject> paymentObjects = usrOnethingMatchings.stream()
                .map(usrOnethingMatching -> tossPaymentFeign.cancelPayment(
                        usrOnethingMatching.getOneThingOrder()
                                .getTossPayment()
                                .stream()
                                .filter(tossPayment -> tossPayment.getTossPaymentStatus().equals(TossPaymentStatus.DONE))
                                .map(TossPayment::getPaymentId)
                                .findFirst()
                                .orElseThrow(IllegalStateException::new),
                        new PaymentCancelDto(CANCEL_REASON))
                ).toList();

        adminPaymentService.updateAllOnethingOrdersStatusAndTossPayment(onethingMatchingId, paymentObjects);
    }

    public void cancelAllRandomOrder(Long randomMatchingId) {
        List<UserRandomMatching> usrRandomMatchings = adminPaymentService.updateRandomMatchingStatusToMatchingFail(randomMatchingId);
        List<PaymentObject> paymentObjects = usrRandomMatchings.stream()
                .map(usrRandomMatching -> tossPaymentFeign.cancelPayment(
                        usrRandomMatching.getRandomOrder().getTossPayment().stream().filter(tossPayment -> tossPayment.getTossPaymentStatus().equals(TossPaymentStatus.DONE))
                                .map(TossPayment::getPaymentId)
                                .findFirst().orElseThrow(IllegalStateException::new),
                        new PaymentCancelDto(CANCEL_REASON)
                )).toList();
        adminPaymentService.updateAllRandomOrdersStatusAndTossPayment(randomMatchingId, paymentObjects);
    }
}
