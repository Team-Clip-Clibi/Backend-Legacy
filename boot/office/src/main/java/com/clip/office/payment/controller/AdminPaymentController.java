package com.clip.office.payment.controller;

import com.clip.office.payment.service.AdminPaymentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class AdminPaymentController {
    private final AdminPaymentFacade adminPaymentFacade;
    @PostMapping("/onethings/{id}/cancel")
    public void cancelOnethingPayment(@PathVariable Long id) {
        adminPaymentFacade.cancelAllOnethingOrder(id);
    }
}
