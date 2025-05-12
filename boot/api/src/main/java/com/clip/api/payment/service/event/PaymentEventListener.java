package com.clip.api.payment.service.event;

import com.clip.api.payment.feign.TossPaymentFeign;
import com.clip.api.payment.feign.dto.PaymentCancelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final TossPaymentFeign tossPaymentFeign;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handlePaymentConfirmed(PaymentExceptionEvent event) {
        tossPaymentFeign.cancelPayment(
                event.getPaymentKey(),
                PaymentCancelDto.builder()
                        .cancelReason("서버 오류료 인한 결제 취소")
                        .build()
        );
    }
}
