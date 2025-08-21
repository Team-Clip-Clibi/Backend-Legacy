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

    /**
     * 원띵 결제 취소 API
     * @param id 원띵 ID
     */
    @PostMapping("/onethings/{id}/cancel")
    public void cancelOnethingPayment(@PathVariable Long id) {
        adminPaymentFacade.cancelAllOnethingOrder(id);
    }

    /**
     * 랜덤 결제 취소 API
     * @param id 랜덤 ID
     */
    @PostMapping("/randoms/{id}/cancel")
    public void cancelRandomPayment(@PathVariable Long id) {
        adminPaymentFacade.cancelAllRandomOrder(id);
    }
}
