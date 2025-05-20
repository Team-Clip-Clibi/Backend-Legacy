package com.clip.api.matching.service;

import com.clip.api.matching.controller.dto.RandomMatchingOrderDto;
import com.clip.api.matching.service.exception.MatchingFailedException;
import com.clip.matching.entity.MatchingStatus;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.entity.RandomMatchingCapacity;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.matching.service.MatchingService;
import com.clip.matching.service.UserRandomMatchingService;
import com.clip.order.entity.RandomOrder;
import com.clip.order.service.RandomOrderService;
import com.clip.price.entity.RandomDiscount;
import com.clip.price.entity.RandomPrice;
import com.clip.price.service.RandomDiscountService;
import com.clip.price.service.RandomPriceService;
import com.clip.user.entity.User;
import com.clip.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RandomMatchingOrderService {
    private final int MIN_RANDOM_MATHING_CAPACITY = 4;
    private final int MIN_AVAILABLE_CAPACITY = 0;

    private final UserService userService;
    private final MatchingService matchingService;
    private final UserRandomMatchingService userRandomMatchingService;
    private final RandomOrderService randomOrderService;
    private final RandomPriceService randomPriceService;
    private final RandomDiscountService randomDiscountService;

    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 30),
            recover = "recoverCreateOrder"
    )
    @Transactional
    public RandomMatchingOrderDto.Response createOrder(long userId, RandomMatchingOrderDto.Request request) {
        User user = userService.findUser(userId);
        List<RandomMatchingCapacity> randomMatchingCapacities = matchingService.findClosestUpcomingRandomMatchingCapacitiesWithDistrict(request.getDistricts());
        RandomPrice basicRandomPrice = randomPriceService.findBasicRandomPrice();
        RandomDiscount baseDiscount = randomDiscountService.findBasicRandomDiscount();

        // 각 모임별 현재 참여자 수 계산 (총 정원 - 가용 정원)
        Map<Long, Integer> currentParticipantsMap = new HashMap<>();
        randomMatchingCapacities.forEach(capacity -> {
            int currentParticipants = capacity.getConfirmedParticipants(capacity.getRandomMatching().getTotalCapacity());
            currentParticipantsMap.put(capacity.getRandomMatching().getId(), currentParticipants);
        });

        // 모든 모임에 4명씩 채워졌는지 확인
        boolean allMeetingsHaveThreeMembers = randomMatchingCapacities.stream()
                .allMatch(capacity -> currentParticipantsMap.get(capacity.getRandomMatching().getId()) >= MIN_RANDOM_MATHING_CAPACITY);

        RandomMatchingCapacity selectedCapacity;

        if (!allMeetingsHaveThreeMembers) {
            // 4명 미만인 모임들 중에서 순서대로 선택 (가용 인원이 많은 순으로 정렬된 상태)
            selectedCapacity = randomMatchingCapacities.stream()
                    .filter(capacity -> currentParticipantsMap.get(capacity.getRandomMatching().getId()) < MIN_RANDOM_MATHING_CAPACITY
                            && capacity.getAvailableCapacity() > MIN_AVAILABLE_CAPACITY)
                    .findFirst()
                    .orElseThrow(() -> new MatchingFailedException("3명 미만인 모임이 없거나 모든 모임이 가득 찼습니다."));
        } else {
            // 모든 모임에 4명씩 채워진 경우, 참여자 수가 가장 적은 모임 선택
            selectedCapacity = randomMatchingCapacities.stream()
                    .filter(capacity -> capacity.getAvailableCapacity() > MIN_AVAILABLE_CAPACITY)
                    .min(Comparator.comparingInt(capacity -> capacity.getConfirmedParticipants(capacity.getRandomMatching().getTotalCapacity())))
                    .orElseThrow(() -> new MatchingFailedException("모든 모임이 가득 찼습니다."));
        }

        RandomMatching assignedMatching = selectedCapacity.getRandomMatching();
        RandomOrder order = randomOrderService.createOrder(user, basicRandomPrice, baseDiscount);

        UserRandomMatching userRandomMatching = UserRandomMatching.builder()
                .user(user)
                .randomMatching(assignedMatching)
                .randomOrder(order)
                .myOneThingContent(request.getTopic())
                .matchingStatus(MatchingStatus.APPLIED)
                .build();

        userRandomMatchingService.save(userRandomMatching);

        // 가용 인원 차감
        boolean reserved = selectedCapacity.reserve();
        if (!reserved) {
            throw new MatchingFailedException("가용 인원 차감에 실패했습니다.");
        }

        return RandomMatchingOrderDto.Response.builder()
                .orderId(order.getOrderId())
                .amount(order.getDiscountedPrice().intValue())
                .meetingTime(assignedMatching.getMeetingTime())
                .meetingPlace(assignedMatching.getRestaurantName())
                .meetingLocation(assignedMatching.getLocation())
                .matchingId(assignedMatching.getId())
                .build();
    }

    @Recover
    public RandomMatchingOrderDto.Response recoverCreateOrder(ObjectOptimisticLockingFailureException e,
                                                              long userId,
                                                              RandomMatchingOrderDto.Request request) {
        throw new MatchingFailedException("랜덤 매칭 처리 중 실패했습니다. 다시 시도해주세요.");
    }

    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    public void restoreCapacity(long userId, long matchingId) {
        RandomMatchingCapacity randomMatchingCapacity = matchingService.findRandomMatchingCapacity(matchingId);
        // 가용 인원 복구
        randomMatchingCapacity.cancelReservation();
    }
}
