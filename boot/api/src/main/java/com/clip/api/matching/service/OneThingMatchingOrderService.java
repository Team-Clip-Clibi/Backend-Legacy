package com.clip.api.matching.service;

import com.clip.api.matching.controller.dto.OneThingOrderDto;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.service.UserOneThingMatchingService;
import com.clip.order.entity.OneThingOrder;
import com.clip.order.service.OneThingOrderService;
import com.clip.user.entity.User;
import com.clip.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OneThingMatchingOrderService {

    private final int ONETHING_MATHING_AMOUNT = 2900;

    private final UserService userService;
    private final UserOneThingMatchingService userOneThingMatchingService;
    private final OneThingOrderService onethingOrderService;

    @Transactional
    public OneThingOrderDto.Response createOrder(long userId, OneThingOrderDto.Request request) {
        
        User user = userService.findUser(userId);
        OneThingOrder order = onethingOrderService.createOrder(user, ONETHING_MATHING_AMOUNT);

        UserOneThingMatching userOneThingMatching = UserOneThingMatching.builder()
                .user(user)
                .myOneThingContent(request.getTopic())
                .myQuizContent(request.getTmiContent())
                .preferredDates(request.getPreferredDates())
                .oneThingBudgetRange(request.getOneThingBudgetRange())
                .build();
        userOneThingMatchingService.save(userOneThingMatching);

        return OneThingOrderDto.Response.builder()
                .amount(ONETHING_MATHING_AMOUNT)
                .orderId(order.getOrderId())
                .build();
    }
}
