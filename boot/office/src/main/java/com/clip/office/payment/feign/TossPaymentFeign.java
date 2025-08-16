package com.clip.office.payment.feign;

import com.clip.global.config.TossFeignConfig;
import com.clip.office.payment.feign.dto.PaymentCancelDto;
import com.clip.office.payment.feign.dto.PaymentObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "tossPayment",
        url = "https://api.tosspayments.com/v1/payments",
        configuration = TossFeignConfig.class
)
public interface TossPaymentFeign {

    @PostMapping("/{paymentKey}/cancel")
    PaymentObject cancelPayment(@PathVariable("paymentKey") String paymentKey, @RequestBody PaymentCancelDto paymentCancelDto);
}
