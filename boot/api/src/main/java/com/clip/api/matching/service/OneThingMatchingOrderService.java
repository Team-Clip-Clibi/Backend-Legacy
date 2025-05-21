package com.clip.api.matching.service;

import com.clip.api.matching.controller.dto.OneThingOrderDto;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.service.UserOneThingMatchingService;
import com.clip.order.entity.OneThingOrder;
import com.clip.order.service.OneThingOrderService;
import com.clip.price.entity.OneThingDiscount;
import com.clip.price.entity.OneThingPrice;
import com.clip.price.service.OneThingDiscountService;
import com.clip.price.service.OneThingPriceService;
import com.clip.user.entity.User;
import com.clip.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OneThingMatchingOrderService {
    private final UserService userService;
    private final UserOneThingMatchingService userOneThingMatchingService;
    private final OneThingOrderService onethingOrderService;
    private final OneThingPriceService oneThingPriceService;
    private final OneThingDiscountService oneThingDiscountService;

    @Transactional
    public OneThingOrderDto.Response createOrder(long userId, OneThingOrderDto.Request request) {

        OneThingPrice basicOneThingPrice = oneThingPriceService.findBasicOneThingPrice();
        OneThingDiscount baseDiscount = oneThingDiscountService.findBaseDiscount();

        User user = userService.findUser(userId);
        OneThingOrder order = onethingOrderService.createOrder(user, basicOneThingPrice, baseDiscount);

        UserOneThingMatching userOneThingMatching = UserOneThingMatching.builder()
                .user(user)
                .oneThingDistrict(request.getDistrict())
                .myOneThingContent(request.getTopic())
                .myQuizContent(request.getTmiContent())
                .preferredDates(request.getPreferredDates())
                .oneThingBudgetRange(request.getOneThingBudgetRange())
                .oneThingOrder(order)
                .oneThingCategory(request.getOneThingCategory())
                .build();
        userOneThingMatchingService.save(userOneThingMatching);

        return OneThingOrderDto.Response.builder()
                .amount(order.getDiscountedPrice().intValue())
                .orderId(order.getOrderId())
                .build();
    }
}
