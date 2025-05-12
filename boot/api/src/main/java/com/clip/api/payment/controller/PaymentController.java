package com.clip.api.payment.controller;

import com.clip.api.docs.payment.PaymentDocs;
import com.clip.api.payment.controller.dto.PaymentDto;
import com.clip.api.payment.service.PaymentServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentDocs {
    private final PaymentServiceFacade paymentServiceFacade;
    @Override
    public void createRandomCommunity(PaymentDto paymentDto, UserDetails userDetails) {
        paymentServiceFacade.confirm(Long.parseLong(userDetails.getUsername()), paymentDto);
    }
}
