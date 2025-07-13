package com.clip.api.matching.service;

import com.clip.api.matching.controller.dto.OneThingOrderDto;
import com.clip.global.exception.InvalidRequestException;
import com.clip.matching.entity.OneThingMatchingStatus;
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

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OneThingMatchingOrderService {
    private static final int RESERVATION_DEADLINE_DAYS = 3;

    private final UserService userService;
    private final UserOneThingMatchingService userOneThingMatchingService;
    private final OneThingOrderService onethingOrderService;
    private final OneThingPriceService oneThingPriceService;
    private final OneThingDiscountService oneThingDiscountService;

    @Transactional
    public OneThingOrderDto.Response createOrder(long userId, OneThingOrderDto.Request request) {
        if (!isAvailableDate(request, LocalDate.now())) {
            throw new InvalidRequestException("The preferred dates must be between 4 days before and 21 days after today.");
        }

        OneThingPrice basicOneThingPrice = oneThingPriceService.findBasicOneThingPrice();
        OneThingDiscount baseDiscount = oneThingDiscountService.findBaseDiscount();

        User user = userService.findUser(userId);
        OneThingOrder order = onethingOrderService.createOrder(user, basicOneThingPrice, baseDiscount);

        UserOneThingMatching userOneThingMatching = UserOneThingMatching.builder()
                .user(user)
                .oneThingDistrict(request.getDistrict())
                .onethingTopic(request.getTopic())
                .tmi(request.getTmiContent())
                .preferredDates(request.getPreferredDates())
                .oneThingBudgetRange(request.getOneThingBudgetRange())
                .oneThingOrder(order)
                .matchingStatus(OneThingMatchingStatus.WAIT_FOR_PAYMENT)
                .oneThingCategory(request.getOneThingCategory())
                .build();
        userOneThingMatchingService.save(userOneThingMatching);

        return OneThingOrderDto.Response.builder()
                .amount(order.getDiscountedPrice().intValue())
                .orderId(order.getOrderId())
                .build();
    }

    private static boolean isAvailableDate(OneThingOrderDto.Request request, LocalDate currentDate) {

        LocalDate requestDate = request.getPreferredDates().stream()
                .sorted()
                .findFirst()
                .orElseThrow()
                .getDate();

        return ((requestDate.getDayOfWeek() == DayOfWeek.SATURDAY) && currentDate.isBefore(requestDate.minusDays(RESERVATION_DEADLINE_DAYS))) ||
                ((requestDate.getDayOfWeek() == DayOfWeek.SUNDAY) && currentDate.isBefore(requestDate.minusDays(RESERVATION_DEADLINE_DAYS)));
    }
}
