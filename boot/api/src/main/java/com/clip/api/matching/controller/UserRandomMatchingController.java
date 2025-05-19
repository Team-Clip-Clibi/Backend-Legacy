package com.clip.api.matching.controller;

import com.clip.api.docs.matching.RandomMatchingDocs;
import com.clip.api.matching.controller.dto.RandomMatchingOrderDto;
import com.clip.api.matching.service.RandomMatchingOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRandomMatchingController implements RandomMatchingDocs {
    private final RandomMatchingOrderService userRandomMatchingService;

    @Override
    public RandomMatchingOrderDto.Response createOrder(RandomMatchingOrderDto.Request request, UserDetails userDetails) {
        return userRandomMatchingService.createOrder(Long.parseLong(userDetails.getUsername()), request);
    }

    @Override
    public void restoreCapacity(UserDetails userDetails, Long orderId) {
        userRandomMatchingService.restoreCapacity(Long.parseLong(userDetails.getUsername()), orderId);
    }
}
