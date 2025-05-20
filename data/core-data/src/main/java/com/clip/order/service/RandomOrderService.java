package com.clip.order.service;

import com.clip.order.entity.RandomOrder;
import com.clip.order.entity.RandomOrderStatus;
import com.clip.order.exception.OrderNotFoundException;
import com.clip.order.repository.RandomOrderRepository;
import com.clip.price.entity.RandomDiscount;
import com.clip.price.entity.RandomPrice;
import com.clip.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RandomOrderService {
    private final RandomOrderRepository randomOrderRepository;

    public RandomOrder createOrder(User user, RandomPrice randomPrice, RandomDiscount randomDiscount) {
        RandomOrder randomOrder = RandomOrder.builder()
                .orderId(UUID.randomUUID())
                .user(user)
                .status(RandomOrderStatus.WAIT_FOR_PAYMENT)
                .randomPrice(randomPrice)
                .randomDiscount(randomDiscount)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build();
        return randomOrderRepository.save(randomOrder);
    }

    public RandomOrder findRandomOrder(long userId, UUID orderId) {
        return randomOrderRepository.findRandomOrder(userId, orderId)
                .orElseThrow(OrderNotFoundException::new);
    }
}
