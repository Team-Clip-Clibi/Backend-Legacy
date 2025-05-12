package com.clip.api.payment.feign;


import com.clip.api.payment.feign.dto.PaymentCancelDto;
import com.clip.api.payment.feign.dto.PaymentConfirmDto;
import com.clip.api.payment.feign.dto.PaymentObject;
import com.clip.global.config.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        value = "tossPayment",
        url = "https://api.tosspayments.com/v1/payments",
        configuration = FeignConfig.class
)
public interface TossPaymentFeign {

    @GetMapping("/{paymentKey}")
    PaymentObject getPayment(@PathVariable("paymentKey") String paymentKey);

    @PostMapping("/confirm")
    PaymentObject confirmPayment(@RequestBody PaymentConfirmDto paymentConfirmDto);

    @PostMapping("/{paymentKey}/cancel")
    PaymentObject cancelPayment(@PathVariable("paymentKey") String paymentKey, @RequestBody PaymentCancelDto paymentCancelDto);
}
